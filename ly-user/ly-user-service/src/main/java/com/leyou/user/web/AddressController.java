package com.leyou.user.web;

import com.leyou.user.dto.AddressDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
public class AddressController {

    /**
     * 根据
     * @param id 地址id
     * @return 地址信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> queryAddressById(@PathVariable("id") Long id){
        AddressDTO address = new AddressDTO();
        address.setId(1L);
        address.setUserId(30L);
        address.setStreet("文荟人才公寓 5号楼");
        address.setCity("苏州");
        address.setDistrict("工业园区");
        address.setAddressee("hmw");
        address.setPhone("18296962694");
        address.setProvince("江苏");
        address.setPostcode("215000");
        address.setIsDefault(true);
        return ResponseEntity.ok(address);
    }
}