package com.maniangdoz.mecapp.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.Query.Direction;
import com.maniangdoz.mecapp.models.Publication;
import com.maniangdoz.mecapp.services.FirebaseInitializer;

@RestController()
@RequestMapping("api/publication")
public class PublicationController {
	
	@Autowired
	FirebaseInitializer db;
	
	@GetMapping("mec/publications")
	public List<Publication> getAllPub() throws InterruptedException, ExecutionException {
		//creation of list of publications
		List<Publication> pubs = new ArrayList<Publication>();
		
		//creation of reference of collection
		CollectionReference publications = db.getFirebase().collection("publication");
		Query orderBy = publications.orderBy("id", Direction.ASCENDING);
		
		//fetch all publications
		ApiFuture<QuerySnapshot> query = orderBy.get();
		QuerySnapshot querySnapshot = query.get();
		
		for(DocumentSnapshot document : querySnapshot.getDocuments()) {
			pubs.add(document.toObject(Publication.class));
		}
		
		return pubs;
	}
	
	@GetMapping("mec/publications/{id}")
	public Publication getPub(@PathVariable("id") long id) throws InterruptedException, ExecutionException {
		
		CollectionReference ref = db.getFirebase().collection("publication");
		Publication value = new Publication();
		ApiFuture<QuerySnapshot> query = ref.get();
		QuerySnapshot querySnapshot = query.get();
		
		for(DocumentSnapshot docs : querySnapshot.getDocuments()) {
			if(docs.get("id").equals(id)) {
				try {
					value = docs.toObject(Publication.class);
					System.out.println("id of current doc " + value.getId());	
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return value;
	}
	
}
