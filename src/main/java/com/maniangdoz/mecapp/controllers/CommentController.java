package com.maniangdoz.mecapp.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.Query.Direction;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.maniangdoz.mecapp.models.Comm;
import com.maniangdoz.mecapp.models.Publication;
import com.maniangdoz.mecapp.services.FirebaseInitializer;

@RestController()
@RequestMapping("api/comment")
public class CommentController {
	
	@Autowired
	FirebaseInitializer db;
	
	@GetMapping("mec/comments")
	public List<Comm> getAllComment() throws InterruptedException, ExecutionException {
		//count all comments of database
		int count = 0;
		
		//list of comments
		List<Comm> comments = new ArrayList<Comm>();
		
		//reference of publications
		CollectionReference comRef = db.getFirebase().collection("publication");
		
		//query to sort result of publications
		Query orderBy = comRef.orderBy("id", Direction.ASCENDING);
		
		
		//fetching of all publication
		ApiFuture<QuerySnapshot> query = orderBy.get();
		QuerySnapshot querySnapshot = query.get();
		
		for(DocumentSnapshot doc : querySnapshot.getDocuments()) {
			comRef = db.getFirebase().collection("publication")
					.document(doc.getId()).collection("comments");
			query = comRef.get();
			querySnapshot = query.get();
			
			for(DocumentSnapshot docComm : querySnapshot.getDocuments()) {
				comments.add(docComm.toObject(Comm.class));
				count++;
			}
		}
		System.out.println(count);
		Collections.sort(comments);
		return comments;
	}
	
	@GetMapping("mec/comments/{id}")
	public Comm getComment(@PathVariable("id") long id) throws InterruptedException, ExecutionException {
		
		CollectionReference ref = db.getFirebase().collection("publication");
		Comm comment = new Comm();
		ApiFuture<QuerySnapshot> query = ref.get();
		QuerySnapshot querySnapshot = query.get();
		List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
		
		for(DocumentSnapshot doc : documents) {
			ref = db.getFirebase().collection("publication")
					.document(doc.getId()).collection("comments");
			
			query = ref.get();
			querySnapshot = query.get();
			
			for(DocumentSnapshot docComm : querySnapshot.getDocuments()) {
				if(docComm.get("id").equals(id)) {
					comment = docComm.toObject(Comm.class);
				}
			}
		}
		
		return comment;
	}
	
	@PostMapping("mec/comments/add/{id}")
	public boolean addComment(@RequestBody Comm comment, 
			@PathVariable("id") long id) throws InterruptedException, ExecutionException {
		boolean result = false;
		String idDoc = "";
		CollectionReference ref = db.getFirebase().collection("publication");
		Publication value = new Publication();
		ApiFuture<QuerySnapshot> query = ref.get();
		QuerySnapshot querySnapshot = query.get();
		
		for(DocumentSnapshot docs : querySnapshot.getDocuments()) {
			if(docs.get("id").equals(id)) {
				idDoc = docs.getId();
				try {
					value = docs.toObject(Publication.class);
					System.out.println("id of current doc " + value.getId());	
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		if(value != null) {
			CollectionReference commentRef = db.getFirebase().collection("publication")
					.document(idDoc).collection("comments");
			commentRef.document().set(comment);
			result = true;
		}
		
		return result;
	}
	
}
