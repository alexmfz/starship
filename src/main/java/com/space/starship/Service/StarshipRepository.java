package com.space.starship.Service;

import com.space.starship.Model.Starship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public interface StarshipRepository extends JpaRepository<Starship, Long> {

    @Query("SELECT a FROM starshipDB a WHERE a.name LIKE %?1%")
    List<Starship> findByName(String name);

    @Transactional
    @Modifying
    @Query("DELETE FROM starshipDB WHERE name =?1")
    public void deleteByName(String name);

    @Transactional
    @Modifying
    @Query("UPDATE starshipDB SET weapons =?2 WHERE name =?1")
    public void modifyStarShip(String name, String weapon);

}
