package coherentsolutions.airapp.controller;

import coherentsolutions.airapp.mapper.PlaneMapper;
import coherentsolutions.airapp.model.dto.AirportDTO;
import coherentsolutions.airapp.model.dto.AllPlanesDTO;

import coherentsolutions.airapp.model.dto.EditPlacementInformationDTO;
import coherentsolutions.airapp.model.dto.PlaneDTO;

import coherentsolutions.airapp.model.entity.Airport;
import coherentsolutions.airapp.model.entity.Plane;

import coherentsolutions.airapp.model.exception.ObjectNotFoundException;
import coherentsolutions.airapp.service.PlaneService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/plane")
@RequiredArgsConstructor
public class PlaneController {

    private final PlaneService planeService;
    private final PlaneMapper planeMapper;

    @GetMapping("/{id}")
    @ApiOperation(value = "Get plane by id", response = PlaneDTO.class, notes = "Plane must exist")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Plane not found", response = ObjectNotFoundException.class),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<PlaneDTO> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                planeMapper.toPlaneDTO(planeService
                        .getById(id)
                        .orElseThrow(() -> new ObjectNotFoundException(id))));
    }

    @GetMapping("/all")
    @ApiOperation(value = "Get all planes", response = PlaneDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    // @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    public AllPlanesDTO getAll() {
        List<PlaneDTO> planeDTOS = planeService.getAll()
                .stream()
                .map(planeMapper::toPlaneDTO)
                .collect(Collectors.toList());
        return new AllPlanesDTO(planeDTOS);
    }

//    @GetMapping("/allAirports")
//    @ApiOperation(value = "Get all planes", response = PlaneDTO.class)
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "OK"),
//            @ApiResponse(code = 400, message = "Bad Request"),
//            @ApiResponse(code = 500, message = "Internal server error")
//    })
//    // @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
//    public Collection<AirportDTO> getAllAirports() {
//        List<Plane> allPlanes = planeService.getAll();
//        Set<AirportDTO> airportSet = new HashSet<>();
//        for (Plane plane : allPlanes) {
//            airportSet.add(plane.getAirport());
//        }
//        return airportSet;
//    }

    @PostMapping("/new")
    @ApiOperation(value = "Create plane", response = PlaneDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Invalid input supplied"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RolesAllowed("ROLE_ADMIN")
    public void createPlane(@Valid @RequestBody PlaneDTO planeDTO) {
        planeService.save(planeMapper.toPlane(planeDTO));
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Delete plane")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Plane not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RolesAllowed("ROLE_ADMIN")
    public void deletePlane(@PathVariable("id") Long id) {
        planeService.delete(id);
    }

    @PutMapping("/edit/{id}")
    @ApiOperation(value = "Edit plane")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RolesAllowed("ROLE_ADMIN")
    public void editPlane(@PathVariable Long id, @Valid @RequestBody PlaneDTO planeDTO) {
        planeService.edit(id, planeDTO);
    }

    @PostMapping("/editPlacementInfo/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public void editPlacementInfo(@PathVariable("id") Long id, @RequestBody EditPlacementInformationDTO editPlacementInformationDTO) {
        Optional<Plane> plane = planeService.getById(id);
        plane.ifPresent(plane1 -> {
            plane1.setDestination(editPlacementInformationDTO.getDestination());
            planeService.save(plane1);
        });
    }
}

