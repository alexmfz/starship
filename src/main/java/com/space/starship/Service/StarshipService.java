package com.space.starship.Service;

import com.space.starship.Exception.GenericException;
import com.space.starship.Model.Starship;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StarshipService {
    private static final Logger logger = Logger.getLogger(StarshipService.class);
    private final StarshipRepository starshipRepository;

    @Autowired
    public StarshipService(StarshipRepository starshipRepository) {
        this.starshipRepository = starshipRepository;
    }

    public List<Starship> getStarships(String pageNo, String pageSize) throws GenericException {
        logger.info("PageNo: " + pageNo);
        logger.info("pageSize: " + pageSize);
        Long pageNoNumber = 0L;
        Long pageSizeNumber = 0L;

        pageNoNumber = pageNo == null ? 0L : Long.valueOf(pageNo);
        pageSizeNumber = pageSize == null ? 1000L : Long.valueOf(pageSize);

        if (pageNoNumber < 0 || pageSizeNumber <= 0) {
            throw new GenericException("It is impossible to retrieve data with negative params!!.");
        }

        Pageable pageable = PageRequest.of(Math.toIntExact(pageNoNumber), Math.toIntExact(pageSizeNumber));
        return starshipRepository.findAll(pageable).get().collect(Collectors.toList());

    }

    public Starship getStarshipById(Long id) throws GenericException{
        if(id <= 0)
            throw new GenericException("It is impossible to retrieve data with negative params!!.");
        return starshipRepository.findById((id)).get();
    }

    public List<Starship> getStarshipByName(String name) {
        return starshipRepository.findByName(name);
    }

    public String createStarship(Starship starship) {
        if (!starshipRepository.findByName(starship.getName()).isEmpty())
            return "Was not possible to create a new starship. " +
                    starship.getName() + " already exists!";
        starshipRepository.save(starship);
        return "A new starship was successfully created!";
    }


    public String modifyStarshipByName(String name, String weapon) {
        if (starshipRepository.findByName(name).isEmpty())
            return "The starship " + name + " is not allocated in BBDD. " +
                    "Modifying was not possible!";
        starshipRepository.modifyStarShip(name, weapon);
        return "The starship " + name + " was modified. New Weapon: " + weapon;
    }

    public String deleteStarshipByName(String name) {
        if (starshipRepository.findByName(name).isEmpty())
            return "The starship " + name + " is not allocated in BBDD!";

        starshipRepository.deleteByName(name);
        return "The starship " + name + " was successfully removed!";
    }
}
