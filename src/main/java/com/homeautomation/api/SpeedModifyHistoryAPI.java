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

import com.homeautomation.dao.SpeedDAO;
import com.homeautomation.dao.SpeedModifyHistoryDAO;
import com.homeautomation.model.FanControlHistory;
import com.homeautomation.model.Speed;
import com.homeautomation.model.SpeedModifyHistory;

@RestController
@RequestMapping("/speedmodifyhistory")
public class SpeedModifyHistoryAPI {
	@PostMapping("")
	public void insertSpeedModifyHistory(@RequestBody FanControlHistory fch) {
		SpeedModifyHistoryDAO smhDAO = new SpeedModifyHistoryDAO();
		SpeedDAO speedDAO = new SpeedDAO();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		for(Speed speed : fch.getFan().getSpeeds()) {
			Speed oldSpeed = speedDAO.getSpeedBySpeedId(speed.getId());
			if(!oldSpeed.getThreshold().equals(speed.getThreshold()) || !oldSpeed.getSpeed().equals(speed.getSpeed())) {
				SpeedModifyHistory smh = new SpeedModifyHistory();
				smh.setHomeOwner(fch.getHomeOwner());
				smh.setThreshold(speed.getThreshold());
				smh.setSpeed(speed.getSpeed());		
				smh.setTime(sdf.format(date));
				smh.setSpeedObject(speed);
				smhDAO.insertSpeedModifyHistory(smh);
			}
			
		}
		
	}
	
	
	@GetMapping("/{id}")
	public Map<String, Object> getSpeedModifyHistoryByFanId(@PathVariable Integer id, @RequestParam(required = false) Integer page) {
		SpeedModifyHistoryDAO tsmhDAO = new SpeedModifyHistoryDAO();
		List<SpeedModifyHistory> smhList = tsmhDAO.getSpeedModifyHistoryByFanId(id);
		List<SpeedModifyHistory> smhDataList = new ArrayList<>();
		if(page == null) {
			page = 1;
		}
		Integer totalPage = (int) (Math.ceil(smhList.size() / 10 + 0.5));
		for(int i=(page-1)*10; i < smhList.size() && i < page*10; i++) {
			smhDataList.add(smhList.get(i));
		}
		Map<String, Object> map = new HashMap<>();
		map.put("data", smhDataList);
		map.put("currentPage", page);
		map.put("totalPage", totalPage);
		return map;
		
	}
}
