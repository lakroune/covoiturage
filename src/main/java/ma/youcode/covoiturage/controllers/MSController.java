package ma.youcode.covoiturage.controllers;

import lombok.RequiredArgsConstructor;
import ma.youcode.covoiturage.services.ConducteurService;
import ma.youcode.covoiturage.services.PassagerService;
import ma.youcode.covoiturage.services.TrajetService;
import ma.youcode.covoiturage.services.VehiculeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ms")
public class MSController {

private  final VehiculeService vehiculeService;
private  final  TrajetService trajetService;

    @GetMapping
    public ResponseEntity<?> ms(){
      return  ResponseEntity.ok( "" );
    }

}
