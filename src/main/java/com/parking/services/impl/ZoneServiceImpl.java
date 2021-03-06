package com.parking.services.impl;

import com.parking.models.DAO.Floor;
import com.parking.models.DAO.ParkingLot;
import com.parking.models.DAO.Zone;
import com.parking.models.DTO.ZoneDTO;
import com.parking.repositories.FloorRepository;
import com.parking.repositories.ZoneRepository;
import com.parking.services.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ZoneServiceImpl implements ZoneService {
    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private FloorRepository floorRepository;

    @Override
    public List<ZoneDTO> getAllZoneDTO(Integer id) {
        if (id == 0) {
            return zoneRepository.findAll().stream().map(this::convertZoneToDTO).collect(Collectors.toList());
        }

        Floor floor = floorRepository.findById(id).orElse(null);
        if (floor != null) {
            return zoneRepository.getZonesByFloor(floor).stream().map(this::convertZoneToDTO).collect(Collectors.toList());
        } else return new ArrayList<>();
    }

    @Override
    public List<ZoneDTO> getAllZoneDTO() {
        return zoneRepository.findAll().stream().map(this::convertZoneToDTO).collect(Collectors.toList());
    }

    @Override
    public void addZone(Zone zone) {
        zoneRepository.save(zone);
    }

    @Override
    public void deleteZone(Integer id) {
        zoneRepository.deleteById(id);
    }

    @Override
    public Zone getZoneById(Integer id) {
        return zoneRepository.findById(id).orElse(null);
    }

    private ZoneDTO convertZoneToDTO(Zone zone) {
        ZoneDTO zoneDTO = new ZoneDTO();
        zoneDTO.setId(zone.getIdZone());
        zoneDTO.setDirection(zone.getDirection());
        zoneDTO.setName(zone.getZoneName());
        zoneDTO.setPositionX(zone.getPositionX());
        zoneDTO.setPositionY(zone.getPositionY());
        zoneDTO.setIdFloor(zone.getFloor().getIdFloor());

        List<Integer> arrIdParkingLot = new ArrayList<>();
        for (ParkingLot p : zone.getListParkingLot()) {
            arrIdParkingLot.add(p.getIdParkingLot());
        }
        zoneDTO.setListParkingLot(arrIdParkingLot);
        zoneDTO.setTypeZone(zone.getTypeZone());
        return zoneDTO;
    }

    @Override
    public Optional<Zone> findByZoneName(String zoneName) {
        return zoneRepository.findByZoneName(zoneName);
    }
}
