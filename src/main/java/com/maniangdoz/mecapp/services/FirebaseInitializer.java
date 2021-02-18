package com.maniangdoz.mecapp.services;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class FirebaseInitializer {
	
	@PostConstruct
	private void initDb() {
		try{
			InputStream serviceAccount =
				new FileInputStream("./serviceAccount.json");

			FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.build();

			FirebaseApp.initializeApp(options);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}

	}
	
	public Firestore getFirebase() {
		return FirestoreClient.getFirestore();
	}

}
