package com.space.starship.Model;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.annotations.OpenAPI31;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Data
@EntityScan
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "starshipDB")
public class Starship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The ID of the starship from 1 to N", example = "1")
    private Long id;
    @Schema(description = "The name of the starship", example = "Halcón Milenario1")
    private String name;
    @Schema(description = "Weapon of the starship", example = "Blaster")
    private String weapons;
}
