package pl.pjwstk.kodabackend.offer.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pjwstk.kodabackend.offer.model.CarEquipmentDto;
import pl.pjwstk.kodabackend.offer.persistance.entity.CarEquipment;

@Component
@RequiredArgsConstructor
public class CarEquipmentMapper {

    public CarEquipmentDto mapToCarEquipmentDto(CarEquipment carEquipment) {
        return new CarEquipmentDto(
                carEquipment.getId(),
                Boolean.TRUE.equals(carEquipment.getAirConditioning()),
                Boolean.TRUE.equals(carEquipment.getAutomaticClimate()),
                Boolean.TRUE.equals(carEquipment.getHeatedSeats()),
                Boolean.TRUE.equals(carEquipment.getElectricSeats()),
                Boolean.TRUE.equals(carEquipment.getLeatherSeats()),
                Boolean.TRUE.equals(carEquipment.getPanoramicRoof()),
                Boolean.TRUE.equals(carEquipment.getElectricWindows()),
                Boolean.TRUE.equals(carEquipment.getElectricMirrors()),
                Boolean.TRUE.equals(carEquipment.getKeylessEntry()),
                Boolean.TRUE.equals(carEquipment.getWheelHeating()),
                Boolean.TRUE.equals(carEquipment.getNavigationSystem()),
                Boolean.TRUE.equals(carEquipment.getBluetooth()),
                Boolean.TRUE.equals(carEquipment.getUsbPort()),
                Boolean.TRUE.equals(carEquipment.getMultifunction()),
                Boolean.TRUE.equals(carEquipment.getAndroidAuto()),
                Boolean.TRUE.equals(carEquipment.getAppleCarPlay()),
                Boolean.TRUE.equals(carEquipment.getSoundSystem()),
                Boolean.TRUE.equals(carEquipment.getParkingSensors()),
                Boolean.TRUE.equals(carEquipment.getRearCamera()),
                Boolean.TRUE.equals(carEquipment.getCruiseControl()),
                Boolean.TRUE.equals(carEquipment.getAdaptiveCruiseControl()),
                Boolean.TRUE.equals(carEquipment.getLaneAssist()),
                Boolean.TRUE.equals(carEquipment.getBlindSpotDetection()),
                Boolean.TRUE.equals(carEquipment.getEmergencyBraking()),
                Boolean.TRUE.equals(carEquipment.getStartStop()),
                Boolean.TRUE.equals(carEquipment.getXenonLights()),
                Boolean.TRUE.equals(carEquipment.getLedLights()),
                Boolean.TRUE.equals(carEquipment.getAmbientLighting()),
                Boolean.TRUE.equals(carEquipment.getAutomaticLights()),
                Boolean.TRUE.equals(carEquipment.getAdapativeLights()),
                Boolean.TRUE.equals(carEquipment.getHeatedSteeringWheel()),
                Boolean.TRUE.equals(carEquipment.getElectricTrunk()),
                Boolean.TRUE.equals(carEquipment.getElectricSunBlind()),
                Boolean.TRUE.equals(carEquipment.getHeadUpDisplay()),
                Boolean.TRUE.equals(carEquipment.getAromatherapy())
        );
    }

}
