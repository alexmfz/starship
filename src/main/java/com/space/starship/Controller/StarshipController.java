package com.space.starship.Controller;

import com.space.starship.Exception.GenericException;
import com.space.starship.Model.ErrorMessage;
import com.space.starship.Model.Starship;
import com.space.starship.Service.StarshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/starship")
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class StarshipController {
    private static final Logger logger = Logger.getLogger(StarshipController.class);
    @Autowired
    StarshipService starshipService;

    /**
     *
     * @param request: Request made
     * @param pageNo [optional]: Describes the init point to look up for H2 DB
     * @param pageSize [optional]: Describes the size to look up for H2 DB
     * @return An array of starships written into H2 DB. All databases are retrieved if params are not set
     */
    @GetMapping("getStarships")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(array = @ArraySchema(
                            schema = @Schema(implementation = Starship.class)))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Operation(description = "Get all starships by pages. It pages is not sent, by default restores all data")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getStarships(HttpServletRequest request,
                                          @Schema(description = "Initial lookup page")
                                          @RequestParam(value = "pageNo", required = false) String pageNo,
                                          @Schema(description = "Lookup size")
                                          @RequestParam(value = "pageSize", required = false) String pageSize) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(starshipService.getStarships(pageNo, pageSize));
        } catch (GenericException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage()));

        }
    }

    /**
     *
     * @param request: Request made
     * @param id: Id of the starship to look for in H2 DB
     * @return: A starship identified by his Id inserted as parameter
     */
    @GetMapping("/getStarshipById")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(
                            schema = @Schema(implementation = Starship.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Operation(description = "Get starship by his ID")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getStarshipsById(HttpServletRequest request,
                                              @Schema(description = "Id of the starship to look for")
                                              @RequestParam(value = "Id", required = true) Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(starshipService.getStarshipById(id));
        } catch (GenericException e){
          e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    /**
     *
     * @param request: Request made
     * @param name: Name of the starship or pattern to look for in the H2 DB
     * @return It retrieves an array of starships which contains the pattern of the input in their name
     */
    @GetMapping("/getStarshipByName")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(
                            schema = @Schema(implementation = Starship.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Operation(description = "Get all starships which contains name")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getStarshipsByName(HttpServletRequest request,
                                                @Schema(description = "Pattern of the starship to look for by name")
                                                @RequestParam(value = "Name", required = true) String name) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(starshipService.getStarshipByName(name));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    /**
     *
     * @param request: Request made
     * @param starship: Object created and inserted by H2 DB
     * @return Describes if the transaction was successfull or not
     */
    @PostMapping("/createStarship")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Operation(description = "Creating a starship to add into the BBDD H2")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createStarship(HttpServletRequest request,
                                            @RequestBody Starship starship) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(starshipService.createStarship(starship));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    /**
     *
     * @param request: Request made
     * @param name: Name of the starship to modify
     * @param weapon: The parameter to set into the starship inserted by his name
     * @return Describes if the transaction was successfull or not
     */
    @PutMapping("/modifyStarshipByName")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Operation(description = "Modify a starship by his name changing the weapon")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> modifyStarshipByName(HttpServletRequest request,
                                                  @Schema(description = "Name of the starship to modify")
                                                  @RequestParam(value = "Name", required = true) String name,
                                                  @Schema(description = "The new weapon to set")
                                                  @RequestParam(value = "Weapon", required = true) String weapon) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(starshipService.modifyStarshipByName(name, weapon));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    /**
     *
     * @param request: Request made
     * @param name: Name of the starship to modify
     * @return Describes if the transaction was successfull or not
     */
    @DeleteMapping("/deleteStarshipByName")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Operation(description = "Delete a starship by his name")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteStarshipByName(HttpServletRequest request,
                                                  @Schema(description = "Name of the starship to delete")
                                                  @RequestParam(value = "Name", required = true) String name) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(starshipService.deleteStarshipByName(name));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

}


