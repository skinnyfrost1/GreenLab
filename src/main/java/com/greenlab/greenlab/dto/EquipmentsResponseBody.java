package com.greenlab.greenlab.dto;
import java.util.List;
import com.greenlab.greenlab.model.Equipment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentsResponseBody {
    private String message;
    private List<Equipment> equipments;
}