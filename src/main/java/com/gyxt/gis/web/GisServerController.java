package com.gyxt.gis.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gyxt.gis.service.GisServerService;

@RestController
//@RequestMapping("/gis-server")
public class GisServerController {
	
	@Autowired
	private GisServerService gisServerService;
	
	@GetMapping(produces = {MediaType.IMAGE_JPEG_VALUE})
	public byte[] show(@RequestParam("layer") String layer,
					   @RequestParam("tilecol") Integer column,
					   @RequestParam("tilerow") Integer row,
					   @RequestParam("tilematrix") Integer level) {
		return gisServerService.show(layer, column, row, level);
	}
}
