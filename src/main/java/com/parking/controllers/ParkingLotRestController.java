package com.parking.controllers;

import com.parking.models.DAO.Floor;
import com.parking.models.DAO.ParkingLot;
import com.parking.models.DAO.Zone;
import com.parking.models.DTO.FloorDTO;
import com.parking.models.DTO.ParkingLotDTO;
import com.parking.models.DTO.ZoneDTO;
import com.parking.services.FloorService;
import com.parking.services.ParkingLotService;
import com.parking.services.ParkingService;
import com.parking.services.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ParkingLotRestController {
    @Autowired
    private FloorService floorService;

    @Autowired
    private ParkingLotService parkingLotService;

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private ParkingService parkingService;

    private String[] arrChar = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "X", "Y"};

    @GetMapping("/getAllParkingLot")
    public ResponseEntity<List<ParkingLotDTO>> getAllParkingLot() {
        return new ResponseEntity<>(parkingLotService.getAllParkingLot(), HttpStatus.OK);
    }

    @GetMapping("/getAllZone/{id}")
    public ResponseEntity<List<ZoneDTO>> getAllZoneByFloor(@PathVariable Integer id) {
        return new ResponseEntity<>(zoneService.getAllZoneDTO(id), HttpStatus.OK);
    }

    @GetMapping("/getAllFloor")
    public ResponseEntity<List<FloorDTO>> getAllFloor() {
        return new ResponseEntity<>(floorService.getAllFloorDTO(), HttpStatus.OK);
    }

    @PostMapping("/saveParkingLot")
    public void saveParkingLot(@RequestBody ParkingLotDTO parkingLotDTO) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setStatusParkingLot(true);
        parkingLot.setIdParkingLot(parkingLotDTO.getId());
        Zone zone = zoneService.getZoneById(parkingLotDTO.getIdZone());
        parkingLot.setZone(zone);
        parkingLotService.addParkingLot(parkingLot);
    }

    @PostMapping("/editParkingLot")
    public void editParkingLot(@RequestBody ParkingLotDTO parkingLotDTO) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setIdParkingLot(parkingLotDTO.getId());
        if (parkingLotDTO.getStatus().equals("Đã có xe")) {
            parkingLot.setStatusParkingLot(false);
        } else {
            parkingLot.setStatusParkingLot(true);
        }
        Zone zone = zoneService.getZoneById(parkingLotDTO.getIdZone());
        parkingLot.setZone(zone);
        parkingLotService.addParkingLot(parkingLot);
    }

    @PostMapping("/saveFloor")
    public ResponseEntity<Floor> saveFloor(@RequestBody List<ZoneDTO> zones) {
        Floor floor = new Floor();
        int id = floorService.getCountOfFloor() + 1;
        floor.setNameFloor("Tầng " + id);
        floorService.addFloor(floor);
        floor = floorService.findByName(floor.getNameFloor());
        for (ZoneDTO zone : zones) {
            Zone zoneSave = new Zone();
            if (zone.getName() == null || zone.getName().equals("")) {
                zoneSave.setZoneName("Khu " + arrChar[(int) (Math.random() * 23)] + arrChar[(int) (Math.random() * 23)]);
            } else {
                zoneSave.setZoneName(zone.getName());
            }
            zoneSave.setDirection(0);
            zoneSave.setFloor(floor);
            zoneSave.setPositionX((int) (Math.random() * 200));
            zoneSave.setPositionY((int) (Math.random() * 300));
            zoneSave.setTypeZone(zone.getTypeZone());
            zoneService.addZone(zoneSave);
        }
        return new ResponseEntity<>(floor, HttpStatus.OK);
    }

    @GetMapping("/getParkingLotById/{id}")
    public ResponseEntity<ParkingLotDTO> getParkingLotById(@PathVariable Integer id) {
        return new ResponseEntity<>(parkingLotService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/getAllCarByDateIn/{dateStart}/{dateEnd}")
    public ResponseEntity<List<String[]>> getAllCarByDateIn(@PathVariable String dateStart, @PathVariable String dateEnd) {
        return new ResponseEntity<>(parkingService.getAllCarByDateIn(dateStart, dateEnd), HttpStatus.OK);
    }

    @GetMapping("/getAllCarByDateOut/{dateStart}/{dateEnd}")
    public ResponseEntity<List<String[]>> getAllCarByDateOut(@PathVariable String dateStart, @PathVariable String dateEnd) {
        return new ResponseEntity<>(parkingService.getAllCarByDateOut(dateStart, dateEnd), HttpStatus.OK);
    }

    @GetMapping("/changeZonePositionX/{id}/{pX}")
    public void changeZonePositionX(@PathVariable Integer id, @PathVariable Integer pX) {
        Zone zone = zoneService.getZoneById(id);
        zone.setPositionX(pX);
        zoneService.addZone(zone);
    }

    @GetMapping("/changeTypeZone/{id}/{value}")
    public void changeTypeZone(@PathVariable Integer id, @PathVariable Integer value) {
        Zone zone = zoneService.getZoneById(id);
        zone.setTypeZone(value);
        zoneService.addZone(zone);
    }

    @GetMapping("/changeZonePositionY/{id}/{pY}")
    public void changeZonePositionY(@PathVariable Integer id, @PathVariable Integer pY) {
        Zone zone = zoneService.getZoneById(id);
        zone.setPositionY(pY);
        zoneService.addZone(zone);
    }

    @GetMapping("/changeDirection/{id}/{dir}")
    public void changeDirection(@PathVariable Integer id, @PathVariable Integer dir) {
        Zone zone = zoneService.getZoneById(id);
        zone.setDirection(dir);
        zoneService.addZone(zone);
    }

    @GetMapping("/changeZoneName/{id}/{name}")
    public void changeZoneName(@PathVariable Integer id, @PathVariable String name) {
        Zone zone = zoneService.getZoneById(id);
        zone.setZoneName(name);
        zoneService.addZone(zone);
    }

    @DeleteMapping("/deleteParkingLot/{id}")
    public void deleteParkingLot(@PathVariable Integer id) {
        parkingLotService.deleteParkingLot(id);
    }

    @GetMapping("/getAllCarByDateInDateOut/{dateStart}/{dateEnd}")
    public ResponseEntity<List<String[]>> getAllCarByDateInDateOut(@PathVariable String dateStart, @PathVariable String dateEnd) {
        return new ResponseEntity<>(parkingService.getAllCarByDateInDateOut(dateStart, dateEnd), HttpStatus.OK);
    }

    @GetMapping("/getAllZone")
    public ResponseEntity<List<ZoneDTO>> getAllZone() {
        return new ResponseEntity<>(zoneService.getAllZoneDTO(), HttpStatus.OK);
    }

    @GetMapping("/deleteZone/{id}")
    public void deleteZone(@PathVariable Integer id) {
        zoneService.deleteZone(id);
    }

    @GetMapping("/deleteFloor/{id}")
    public void deleteFloor(@PathVariable Integer id) {
        floorService.deleteFloor(id);
    }

    @GetMapping("/addZone/{id}")
    public void addZone(@PathVariable Integer id){
        Zone zone = new Zone();
        zone.setTypeZone(0);
        zone.setZoneName("Khu " + arrChar[(int) (Math.random() * 23)] + arrChar[(int) (Math.random() * 23)]);
        zone.setDirection(0);
        zone.setPositionX((int) (Math.random() * 200));
        zone.setPositionY((int) (Math.random() * 300));
        zone.setFloor(floorService.findById(id));
        zoneService.addZone(zone);
    }
}