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
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.Query.Direction;
import com.google.cloud.firestore.QuerySnapshot;
import com.maniangdoz.mecapp.models.Comm;
import com.maniangdoz.mecapp.models.Surfer;
import com.maniangdoz.mecapp.services.FirebaseInitializer;

@RestController()
@RequestMapping("api/surfer")
public class SurferController {
	@Autowired
	FirebaseInitializer db;
	
	@GetMapping("mec/surfers")
	public List<Surfer> getAllSurfer() throws InterruptedException, ExecutionException {
		int count = 0;
		List<Surfer> surfers = new ArrayList<Surfer>();
		CollectionReference publications = db.getFirebase().collection("publication");
		Query orderby = publications.orderBy("id", Direction.ASCENDING);
		ApiFuture<QuerySnapshot> query = orderby.get();
		QuerySnapshot querySnapshot = query.get();
		
		for(DocumentSnapshot doc : querySnapshot.getDocuments()) {
			publications = db.getFirebase().collection("publication")
					.document(doc.getId()).collection("comments");
			
			orderby = publications.orderBy("id", Direction.ASCENDING);
			query = orderby.get();
			querySnapshot = query.get();
			
			for(DocumentSnapshot doc2 : querySnapshot.getDocuments()) {
				publications = db.getFirebase().collection("publication")
						.document(doc.getId()).collection("comments")
						.document(doc2.getId()).collection("surfers");
				
				orderby = publications.orderBy("id", Direction.ASCENDING);
				query = orderby.get();
				querySnapshot = query.get();
				
				for(DocumentSnapshot doc3 : querySnapshot.getDocuments()) {
					surfers.add(doc3.toObject(Surfer.class));
					count++;
				}
			}
		}
		System.out.println(count);
		Collections.sort(surfers);
		return surfers;
	}
	
	@GetMapping("mec/surfers/{id}")
	public Surfer getSurfer(@PathVariable("id") long id) throws InterruptedException, ExecutionException {
		int count = 0;
		Surfer surfer = new Surfer();
		CollectionReference publications = db.getFirebase().collection("publication");
		Query orderby = publications.orderBy("id", Direction.ASCENDING);
		ApiFuture<QuerySnapshot> query = orderby.get();
		QuerySnapshot querySnapshot = query.get();
		
		for(DocumentSnapshot doc : querySnapshot.getDocuments()) {
			publications = db.getFirebase().collection("publication")
					.document(doc.getId()).collection("comments");
			
			orderby = publications.orderBy("id", Direction.ASCENDING);
			query = orderby.get();
			querySnapshot = query.get();
			
			for(DocumentSnapshot doc2 : querySnapshot.getDocuments()) {
				publications = db.getFirebase().collection("publication")
						.document(doc.getId()).collection("comments")
						.document(doc2.getId()).collection("surfers");
				
				orderby = publications.orderBy("id", Direction.ASCENDING);
				query = orderby.get();
				querySnapshot = query.get();
				
				for(DocumentSnapshot doc3 : querySnapshot.getDocuments()) {
					if(doc3.get("id").equals(id)) {
						surfer = doc3.toObject(Surfer.class);
					}
					count++;
				}
			}
		}
		System.out.println(count);
		return surfer;
	}
	
	@PostMapping("mec/surfers/add/{id}")
	public boolean addSurfer(@RequestBody Surfer surfer, @PathVariable("id") long id) throws InterruptedException, ExecutionException {
		boolean result = false;
		String idComm = "";
		String idPub = "";
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
					idPub = doc.getId();
					idComm = docComm.getId();
					comment = docComm.toObject(Comm.class);
				}
			}
		}
		
		if(comment != null) {
			ref = db.getFirebase().collection("publication")
					.document(idPub).collection("comments")
					.document(idComm).collection("surfers");
			
			ref.document().set(surfer);
			result = true;
		}
		
		return result;
	}
}
