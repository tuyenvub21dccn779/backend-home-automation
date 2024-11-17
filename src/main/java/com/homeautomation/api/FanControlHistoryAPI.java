package com.homeautomation.api;

import java.text.SimpleDateFormat;
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

import com.homeautomation.dao.BulbControlHistoryDAO;
import com.homeautomation.dao.FanControlHistoryDAO;
import com.homeautomation.dto.BulbControlHistoryDTO;
import com.homeautomation.dto.FanControlHistoryDTO;
import com.homeautomation.model.BulbControlHistory;
import com.homeautomation.model.FanControlHistory;

@RestController
@RequestMapping("/fancontrolhistory")
public class FanControlHistoryAPI {
	@PostMapping("")
	public void insertFanControlHistory(@RequestBody FanControlHistory fch) {
		FanControlHistoryDAO fchDAO = new FanControlHistoryDAO();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		fch.setMode(fch.getFan().getMode());
		fch.setState(fch.getFan().getState());
		
		if(fch.getState() == 0) {
			fch.setThreshold(fch.getFan().getSpeeds().get(1).getThreshold());
		} else {
			fch.setThreshold(fch.getFan().getSpeeds().get(fch.getState()-1).getThreshold());
		}
		fch.setStarttime(sdf.format(date));
		fch.setEndtime(sdf.format(date));
		fchDAO.insertFanControlHistory(fch);
	}
	
	@GetMapping("/{id}")
	public Map<String, Object> getFanControlHistoryByFanId(@PathVariable Integer id, @RequestParam(required = false) Integer page) {
		FanControlHistoryDAO fchDAO = new FanControlHistoryDAO();
		List<FanControlHistory> fchList = fchDAO.getFanControlHistoryByFanId(id);
		List<FanControlHistoryDTO> fchDtoList = new ArrayList<>();
		if(page == null) {
			page = 1;
		}
		Integer totalPage = (int) (Math.ceil(fchList.size() / 10 + 0.5));
		for(int i=(page-1)*10; i < fchList.size() && i < page*10; i++) {
			FanControlHistoryDTO fchDTO = new FanControlHistoryDTO();
			fchDTO.setId(fchList.get(i).getId());
			fchDTO.setStarttime(fchList.get(i).getStarttime());
			fchDTO.setEndtime(fchList.get(i).getEndtime());
			fchDTO.setMode(fchList.get(i).getMode());
			fchDTO.setState(fchList.get(i).getState());
			fchDTO.setThreshold(fchList.get(i).getThreshold());
			fchDtoList.add(fchDTO);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("data", fchDtoList);
		map.put("currentPage", page);
		map.put("totalPage", totalPage);
		return map;
		
	}
}
