package com.homeautomation.api;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.homeautomation.dao.SensorDataDAO;
import com.homeautomation.dto.SensorDataDTO;
import com.homeautomation.model.SensorData;

@RestController
@RequestMapping("/sensordata")
public class SensorDataAPI {
	@PostMapping() 
	public void insertData(@RequestBody SensorData sensorData) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sensorData.setTime(sdf.format(date));
		SensorDataDAO sensorDataDAO = new SensorDataDAO();
		sensorDataDAO.insertSensorData(sensorData);
	}
	
	@GetMapping("/{id}")
	public Map<String, Object> getDataBySensorId(@PathVariable Integer id, @RequestParam(required = false) Integer page) {
		SensorDataDAO sdDAO = new SensorDataDAO();
		List<SensorData> sensorDataList = sdDAO.getDataBySensorId(id);
		List<SensorDataDTO> sensorDataDtoList = new ArrayList<>();
		if(page == null) {
			page = 1;
		}
		Integer totalPage = (int) (Math.ceil(sensorDataList.size() / 10 + 0.5));
		for(int i=(page-1)*10; i < sensorDataList.size() && i < page*10; i++) {
			SensorDataDTO sdDTO = new SensorDataDTO();
			sdDTO.setId(sensorDataList.get(i).getId());
			sdDTO.setTime(sensorDataList.get(i).getTime());
			sdDTO.setValue(sensorDataList.get(i).getValue());
			sensorDataDtoList.add(sdDTO);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("data", sensorDataDtoList);
		map.put("currentPage", page);
		map.put("totalPage", totalPage);
		return map;
		
	}
	
	@GetMapping("/{id}/newest")
	public SensorDataDTO getNewestDataBySensorId(@PathVariable Integer id) {
		SensorDataDAO sdDAO = new SensorDataDAO();
		SensorDataDTO sdDTO = new SensorDataDTO();
		SensorData sensorData = sdDAO.getNewestDataBySensorId(id);
		sdDTO.setId(sensorData.getId());
		sdDTO.setTime(sensorData.getTime());
		sdDTO.setValue(sensorData.getValue());
		return sdDTO;
		
	}
}
