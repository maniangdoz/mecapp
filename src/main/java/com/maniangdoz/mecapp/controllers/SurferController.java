package com.maniangdoz.mecapp.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maniangdoz.mecapp.models.Surfer;

@RestController()
@RequestMapping("api/surfer")
public class SurferController {
	
	@GetMapping("mec/surfers")
	public List<Surfer> getAllSurfer() {
		return null;
	}
	
	@GetMapping("mec/surfers/{id}")
	public Surfer getSurfer(@PathVariable("id") long id) {
		return new Surfer();
	}
}
