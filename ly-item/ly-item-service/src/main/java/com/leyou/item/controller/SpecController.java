package com.leyou.item.controller;

import com.leyou.item.dto.SpecGroupDTO;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.entity.SpecGroup;
import com.leyou.item.entity.SpecParam;
import com.leyou.item.service.SpecGroupService;
import com.leyou.item.service.SpecParamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Huang Mingwang
 * @create 2021-05-26 5:13 下午
 */
@RestController
@RequestMapping("/spec")
public class SpecController {
    private final SpecGroupService specGroupService;
    private final SpecParamService specParamService;

    public SpecController(SpecGroupService specGroupService, SpecParamService specParamService) {

        this.specGroupService = specGroupService;
        this.specParamService = specParamService;
    }

    /**
     * 根据商品分类id查询规格组
     *
     * @param id 商品分类id
     * @return 规格组集合
     */
    @GetMapping("/groups/of/category")
    public ResponseEntity<List<SpecGroupDTO>> querySpecGroupByCategoryId(@RequestParam("id") Long id) {
        List<SpecGroup> groupList = specGroupService.lambdaQuery()
                .eq(SpecGroup::getCategoryId, id).list();
        return ResponseEntity.ok(SpecGroupDTO.convertEntityList(groupList));
    }

    /**
     * 根据条件查询规格参数集合
     *
     * @param categoryId 分类id
     * @param groupId    规格组id
     * @param searching  是否搜索
     * @return 参数列表
     */
    @GetMapping("/params")
    public ResponseEntity<List<SpecParamDTO>> querySpecParams(@RequestParam(value = "categoryId", required = false) Long categoryId,
                                                              @RequestParam(value = "groupId", required = false) Long groupId,
                                                              @RequestParam(value = "searching", required = false) Boolean searching) {
        List<SpecParamDTO> paramList = specParamService.querySpecParams(categoryId, groupId, searching);
        return ResponseEntity.ok(paramList);
    }

    /**
     * 新增规格组
     *
     * @param specGroupDTO 规格组信息
     * @return 无
     */
    @PostMapping("/group")
    public ResponseEntity<Void> saveSpecGroup(@RequestBody SpecGroupDTO specGroupDTO) {
        specGroupService.save(specGroupDTO.toEntity(SpecGroup.class));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改规格组
     *
     * @param specGroupDTO 规格组信息
     * @return 无
     */
    @PutMapping("/group")
    public ResponseEntity<Void> updateSpecGroup(@RequestBody SpecGroupDTO specGroupDTO) {
        specGroupService.updateById(specGroupDTO.toEntity(SpecGroup.class));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 新增规格参数
     *
     * @param specParamDTO 规格组信息
     * @return 无
     */
    @PostMapping("/param")
    public ResponseEntity<Void> saveSpecParam(@RequestBody SpecParamDTO specParamDTO) {
        specParamService.save(specParamDTO.toEntity(SpecParam.class));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改规格参数
     *
     * @param specParamDTO 规格组信息
     * @return 无
     */
    @PutMapping("/param")
    public ResponseEntity<Void> updateSpecParam(@RequestBody SpecParamDTO specParamDTO) {
        specParamService.updateById(specParamDTO.toEntity(SpecParam.class));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 根据分类id查询规格组及组内参数
     *
     * @param id 商品分类id
     * @return 规格组集合
     */
    @GetMapping("/list")
    public ResponseEntity<List<SpecGroupDTO>> querySpecGroupDetailByCategoryId(@RequestParam("id") Long id) {
        List<SpecGroupDTO> groupDTOList = specGroupService.querySpecGroupDetailByCategoryId(id);
        return ResponseEntity.ok(groupDTOList);
    }
}
