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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.homeautomation.dao.BulbControlHistoryDAO;
import com.homeautomation.dto.BulbControlHistoryDTO;
import com.homeautomation.model.BulbControlHistory;

@RestController
@RequestMapping("/bulbcontrolhistory")
public class BulbControlHistoryAPI {
	@PostMapping("")
	public void insertBulbControlHistory(@RequestBody BulbControlHistory bch) {
		BulbControlHistoryDAO bchDAO = new BulbControlHistoryDAO();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		bch.setMode(bch.getBulb().getMode());
		bch.setState(bch.getBulb().getState());
		bch.setStarttime(sdf.format(date));
		bch.setEndtime(sdf.format(date));
		bchDAO.insertBulbControlHistory(bch);
	}
	
	
	@GetMapping("/{id}")
	public Map<String, Object> getBulbControlHistoryByBulbId(@PathVariable Integer id, @RequestParam(required = false) Integer page) {
		BulbControlHistoryDAO bchDAO = new BulbControlHistoryDAO();
		List<BulbControlHistory> bchList = bchDAO.getBulbControlHistoryByBulbId(id);
		List<BulbControlHistoryDTO> bchDtoList = new ArrayList<>();
		if(page == null) {
			page = 1;
		}
		Integer totalPage = (int) (Math.ceil(bchList.size() / 10 + 0.5));
		for(int i=(page-1)*10; i < bchList.size() && i < page*10; i++) {
			BulbControlHistoryDTO bchDTO = new BulbControlHistoryDTO();
			bchDTO.setId(bchList.get(i).getId());
			bchDTO.setStarttime(bchList.get(i).getStarttime());
			bchDTO.setEndtime(bchList.get(i).getEndtime());
			bchDTO.setMode(bchList.get(i).getMode());
			bchDTO.setState(bchList.get(i).getState());
			bchDtoList.add(bchDTO);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("data", bchDtoList);
		map.put("currentPage", page);
		map.put("totalPage", totalPage);
		return map;
		
	}
}
