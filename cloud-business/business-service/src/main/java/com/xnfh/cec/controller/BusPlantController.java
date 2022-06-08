package com.xnfh.cec.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.xnfh.cec.anno.Permission;
import com.xnfh.cec.repository.BusPlantRepository;
import com.xnfh.cec.repository.specification.PlantSpecification;
import com.xnfh.cec.repository.specification.PlantTypeSpecification;
import com.xnfh.cec.repository.specification.UnitSpecification;
import com.xnfh.cec.service.BusPlantService;
import com.xnfh.common.ApiResponse;
import com.xnfh.dto.ImportFileDto;
import com.xnfh.entity.BusPlant;
import com.xnfh.exception.ApiException;
import com.xnfh.vo.PlantVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/23
 */
@RestController
@RequestMapping(value = "/v1/xnfh/bus")
@Api(tags = "作物管理控制器")
public class BusPlantController extends CCAbstractController {


    @Value("${rest.page.size.max}")
    private Integer pageSizeMax;

    @Autowired
    private BusPlantRepository busPlantRepository;

    @Autowired
    private BusPlantService busPlantService;


    @ApiOperation(value = "查询所有作物列表")
    @RequestMapping(value = "/plant/list",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "plantVo传输对象",value = "plantVo作物传输对象")
    })
    @Permission
    public ResponseEntity<ApiResponse> getPlantList( PlantVo plantVo,
                                                    @PageableDefault(page = 0, size = 10) Pageable pageable){
        Pageable pageRequest  = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), PlantSpecification.getSort());
        Page<BusPlant> busPlantPage =  busPlantService.getPlantPage(plantVo,pageRequest);
        return responsePage(busPlantPage);
    }


    @ApiOperation(value = "新增作物")
    @RequestMapping(value = "/plant/add", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "busPlant",value = "busPlant")
    })
    public ResponseEntity<ApiResponse> externalAdd(@RequestBody BusPlant busPlant) {
        return response(busPlantService.externalAdd(busPlant));
    }


    @ApiOperation(value = "编辑作物")
    @RequestMapping(value = "/plant/update", method = RequestMethod.PUT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "busPlant",value = "busPlant")
    })
    public ResponseEntity<ApiResponse> externalUpdate(@RequestBody BusPlant busPlant) {
        busPlantService.externalUpdate(busPlant);
        return response();
    }

    @ApiOperation(value = "根据主键id查询")
    @RequestMapping(value = "/plant/{plantId}",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name ="plantId",value ="plantId主键id" )
    })
    public ResponseEntity<ApiResponse> findPlantByPlantId(@PathVariable("plantId") Integer plantId){
        BusPlant busPlant = busPlantService.findPlantByPlantId(plantId);
        if(busPlant !=null){
            return response(busPlant);
        }
        return response();
    }


    @ApiOperation(value = "批量删除作物信息")
    @RequestMapping(value = "/plant/batchdelete", method = RequestMethod.DELETE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "批量删除作物信息")
    })
    public ResponseEntity<ApiResponse> batchDelete(@RequestBody List<Integer> ids) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            busPlantService.batchDelete(ids);
            apiResponse.setStatus(ApiResponse.STATUS_OK);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (ApiException e) {
            apiResponse.setStatus(ApiResponse.STATUS_FAIL);
            apiResponse.setErrorCode(e.getErrorCode());
            apiResponse.setErrorDescription(e.getErrorDescription());
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
    }

    @ApiOperation(value = "批量导入作物信息")
    @RequestMapping(value = "/plant/import", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "impo" +
                    "" +
                    "" +
                    "rt",value = "批量导入作物信息")
    })
    public Map<String, Object> importExcel(@RequestParam("file") MultipartFile file){
        Map<String, Object> map = new HashMap<>();
        try {
            map = busPlantService.importExcel(file);
        }catch (Exception e){
            map.put("status",-1);
            map.put("data", "导入异常");
            logger.error(e.getMessage(),e);
        }

        return map;
    }

//    @RequestMapping(value = "/excel", method = RequestMethod.POST)
//    @ApiOperation(value = "用户表导入",notes = "导入demo")
    public boolean  upload(@RequestBody ImportFileDto file) throws IOException, InvalidFormatException, org.apache.poi.openxml4j.exceptions.InvalidFormatException, ParseException {

        List<BusPlant> list = new ArrayList<BusPlant>();
        //1、得到上传的表
        if (file.equals(null) || "".equals(file)){
            System.out.println("file为空 = " + file);
            return false;
        }
        Workbook workbook = WorkbookFactory.create(file.getFile().getInputStream());
        //2、获取用户表
        Sheet sheet = workbook.getSheet("Sheet1");
        //3、获取表的总行数
        int num = sheet.getLastRowNum();
        System.out.println("总行数为 = " + num);
        //4、总列数
        int count = sheet.getRow(0).getLastCellNum();
        System.out.println("总列数为 = " + count);
        BusPlant  sysAdminUser = new BusPlant();
        //遍历每一行数据
        for (int j = 1; j <= num; j++) {
            // 读取cell单元格内容
            BusPlant tblFixChange = new BusPlant();
            Row row = sheet.getRow(j);
            Integer plantId = Integer.valueOf(getCellValue(row.getCell(0)));
            tblFixChange.setPlantId(plantId);

            String plantName = getCellValue(row.getCell(1));
            tblFixChange.setPlantName(plantName);

            String remark = getCellValue(row.getCell(2));
            tblFixChange.setRemark(remark);

            Integer plantCode = Integer.valueOf(getCellValue(row.getCell(3)));
            tblFixChange.setPlantCode(String.valueOf(plantCode));

            Integer plantTypeId = Integer.valueOf(getCellValue(row.getCell(4)));
            tblFixChange.setPlantTypeId(plantTypeId);

            String cellValue = getCellValue(row.getCell(5));
            Date update = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cellValue);
            tblFixChange.setCreateTime(update);


            String create = getCellValue(row.getCell(6));
            Date created = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(create);
            tblFixChange.setCreateTime(created);
            list.add(tblFixChange);
            busPlantRepository.saveAll(list);
        }
        return true;
    }

    /**
     * 获取每个单元格内容
     *
     * @param cell cell
     * @return String
     */
    private String getCellValue(Cell cell) {
        // 单元格内容
        String value = "";
        if (cell != null) {
            // 以下是判断数据的类型
            switch (cell.getCellType()) {
                // 数字
                case HSSFCell
                        .CELL_TYPE_NUMERIC:
                    value = cell.getNumericCellValue() + "";
                    // 判断cell是不是日期类型的
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        // 获取日期类型的单元格
                        Date date = cell.getDateCellValue();
                        if (null != date) {
                            value = new SimpleDateFormat("yyyy-MM-dd").format(date);
                        } else {
                            value = "";
                        }
                    } else {
                        value = new DecimalFormat("0").format(cell.getNumericCellValue());
                    }
                    break;
                // 字符串
                case HSSFCell.CELL_TYPE_STRING:
                    value = cell.getStringCellValue();
                    break;
                // Boolean
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    value = cell.getBooleanCellValue() + "";
                    break;
                // 公式
                case HSSFCell.CELL_TYPE_FORMULA:
                    value = cell.getCellFormula() + "";
                    break;
                // 空格
                case HSSFCell.CELL_TYPE_BLANK:
                    value = "";
                    break;
                // 错误
                case HSSFCell.CELL_TYPE_ERROR:
                    value = "非法字符";
                default:
                    value = "未知类型";
                    break;
            }
        }
        return value.trim();
    }


}
