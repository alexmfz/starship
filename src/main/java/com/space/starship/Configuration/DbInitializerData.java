package com.space.starship.Configuration;

import com.space.starship.Model.Starship;
import com.space.starship.Service.StarshipRepository;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DbInitializerData {
    private static final Logger logger = LogManager.getLogger(DbInitializerData.class);
    @Autowired
    private StarshipRepository starshipRepository;

    @PostConstruct
    private void postConstruct(){
        logger.info("Inserting data to initialize BBDD H2 with 10 starships");
        Starship starship_1 = new Starship();
        starship_1.setName("Atlantis");
        starship_1.setWeapons("Cannons");
        starshipRepository.save(starship_1);

        Starship starship_2 = new Starship();
        starship_2.setName("Discovery");
        starship_2.setWeapons("Bluster");
        starshipRepository.save(starship_2);

        Starship starship_3 = new Starship();
        starship_3.setName("Corazon de Oro");
        starship_3.setWeapons("Laser");
        starshipRepository.save(starship_3);

        Starship starship_4 = new Starship();
        starship_4.setName("Enano Rojo");
        starship_4.setWeapons("Gravity");
        starshipRepository.save(starship_4);

        Starship starship_5 = new Starship();
        starship_5.setName("Enterprise");
        starship_5.setWeapons("Fire");
        starshipRepository.save(starship_5);

        Starship starship_6 = new Starship();
        starship_6.setName("Estrella de la Muerte");
        starship_6.setWeapons("Cannons");
        starshipRepository.save(starship_6);

        Starship starship_7 = new Starship();
        starship_7.setName("Galáctica");
        starship_7.setWeapons("Light");
        starshipRepository.save(starship_7);

        Starship starship_8= new Starship();
        starship_8.setName("Halcón Milenario");
        starship_8.setWeapons("Fire");
        starshipRepository.save(starship_8);

        Starship starship_9 = new Starship();
        starship_9.setName("Tardis");
        starship_9.setWeapons("Cannons");
        starshipRepository.save(starship_9);

        Starship starship_10 = new Starship();
        starship_10.setName("Odissey");
        starship_10.setWeapons("Laser");
        starshipRepository.save(starship_10);

    }
}
