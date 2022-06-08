package com.xnfh.exception;

import lombok.Getter;

@Getter
public enum ExceptionDefinition {

    RESPONSE_BODY_EMPTY_ERROR_204(204,"no content"),
    GET_CALL_IPADDRESS_NOT_FOUND_4001(4001, "get ipAddress not found."),
    GET_CALL_IPADDRESS_ERROR_4002(4002, "get ipAddress is error."),
    GET_CALL_REFERER_ERROR_4003(4003, "get ipAddress is error."),
    CURRENT_CHOICE_STATUS_NOT_EXIST_4004(4004, "current choice status not exist."),
    CURRENT_CHECK_PARAMS_TRUENAME_ERROR_4005(4005, "current check params truename error."),
    CURRENT_CHECK_PARAMS_ACCOUNTNAME_ERROR_4006(4006, "current check params accountName error."),
    CURRENT_CHECK_PARAMS_PASSWORD_ERROR_4007(4007, "current check params password too long ."),
    CURRENT_CHECK_PARAMS_ERROR_4008(4008, "current check params error ."),
    CURRENT_CHECK_PARAMS_BUSINESS_ERROR_4009(4009, "current check params business error ."),
    CURRENT_PASSWORD_ERROR_4010(4010, "current check password error ."),
    CURRENT_CODE_INPUT_ERROR_4011(4011, "current code input error ."),
    INSERT_SAVE_RESOURCE_TYPE_EXIST_4012(4012, "insert save resource type exist ."),
    //作物管理
    INSERT_PLANT_type_ID_NO_CHOICE_4013(4013, "insert plant type id is no choice ."),
    INSERT_PLANT_NAME_PARAMS_ERROR_4014(4014, "insert plant name or length error ."),
    INSERT_PLANT_NAME_ALREADY_EXIST_4015(4015, "insert plant name already exist ."),
    INSERT_PLANT_CODE_PARAMS_ERROR_4016(4016, "insert plant code params error ."),
    UPDATE_DATA_DB_NOT_EXIST_4017(4017, "update data db not exist ."),
    UPDATE_DATA_ERROR_4018(4018, "update data error ."),
    GET_PLANT_DATA_NOT_EXIST_4019(4019, "get plant data not exist ."),

    //资源登记
    GET_PLANT_RESOURCE_RESOURCE_ID_NOT_EXIST_4020(4020, "get plant resource resourceId not exist ."),
    GET_PLANT_RESOURCE_PLANT_TYPE_ID_AND_PLANT_ID_IS_NULL_4021(4021, "get plant resource type and plant is null ."),
    GET_PLANT_RESOURCE_NAME_AND_CODE_IS_NULL_4022(4022, "get plant resource name and code is null ."),
    GET_PLANT_RESOURCE_LATIN_NAME_IS_NULL_4023(4023, "get plant resource latin message  is null ."),
    GET_PLANT_RESOURCE_CHINESE_MESSAGE_IS_NULL_4024(4024, "get plant resource chinese message  is null ."),
    GET_PLANT_SAVE_RESOURCE_IS_NULL_4025(4025, "get plant resource save resource  is null ."),
    GET_PLANT_IS_OPEN_CONTACT_PHONE_IS_NULL_4026(4026, "get plant resourcecontact nad phone is null ."),
    GET_PLANT_OTHER_CERE_AND_REMARK_LENGTH_MORE_THAN_4027(4027, "get plant other and remark more than 500."),

    //撤销
    GET_PLANT_CANCEL_PLANT_RESOURCE_INPUTMUN_NOT_EXIST_4028(4028, "get plant cancel plant resource inputNum not exist."),
    GET_PLANT_CANCEL_PLANT_RESOURCE_DB_NOT_EXIST_4029(4029, "get plant cancel plant resource DB not exist."),
    //用户端用户信息
    GET_SYSUSER_SEARCH_DB_NOT_EXIST_4030(4030, "get sysUser data DB not exist."),
    //反馈
    GET_PHONE_DATA_REGEX_NOT_CORRECT_4031(4031, "get phone data regex not correct."),
    GET_BUS_FEEDBACK_CONTENT_TO_LONG_4032(4032, "get bus feedBack data content too long,should be less 200."),
    GET_BUS_FEEDBACK_CONTENT_IS_NULL_4033(4033, "get bus feedBack data content is null."),
    GET_CALL_IPADDRESS_NOT_FOUND_4034(4034, "get ipAddress not found."),
    GET_BACK_FEED_DATA_DB_NOT_EXIST_4036(4036, "get feed back data db not exist."),
    //文件上传
    CURRENT_UPLOAD_IMG_NOT_CORRECT_4035(4035, "current upload file img not correct,please rebase upload."),

    //专家管理
    CURRENT_OPERATE_DATA_USERNAME_NOT_EXIST_4037(4037, "current operate data userName is null."),
    CURRENT_OPERATE_DATA_USERNAME_lENGTH_TO_LONG_4038(4038, "current operate data userName length to long."),
    CURRENT_OPERATE_DATA_USERNAME_EXIST_4039(4039, "current operate data userName exist."),
    CURRENT_OPERATE_DATA_EXPERTNAME_IS_NULL_4040(4040, "current operate data expert is null."),
    CURRENT_OPERATE_DATA_EXPERTNAME_lENGTH_TO_LONG_4041(4041, "current operate data expert length to long."),
    CURRENT_OPERATE_DATA_EXPERTNAME_MUST_INPUT_HANZI_4042(4042, "current operate data expertName must input hanzi."),
    CURRENT_OPERATE_DATA_EXPERTPHONE_IS_EXIST_4043(4043, "current operate data expertPhone is exist."),
    CURRENT_OPERATE_DATA_EXPERT_EMAIL_IS_PARAM_ERROR_4044(4044, "current operate data expert email is param error."),
    CURRENT_OPERATE_DATA_EXPERT_IDCARD_IS_PARAM_ERROR_4045(4045, "current operate data expert id card is param error."),
    CURRENT_SEARCH_SYSEXPERT_DATA_DB_IS_NULL(4046, "current search sysExpert data db is null."),
    CURRENT_SEARCH_SYSUSER_PERSONAL_IS_NULL_4077(4047, "current search sysUser personal is null."),

    //作物类型
    CURRENT_OPERATE_ADD_PLANT_TYPE_TYPE_NAME_IS_NULL_4078(4078,"current operate add plantType typeName is null"),
    CURRENT_OPERATE_SEARCH_PLANT_TYPE_IS_NULL_4079(4079,"current operate add plantType is null"),
    CURRENT_OPERATE_BUS_PLANTTYPE_NAME_TOO_LONG_4080(4080,"current operate plantType name too long"),
    CURRENT_OPERATE_BUS_PLANTTYPE_NAME_IS_EXIST_4081(4081,"current operate plantType name is exist"),

    //性状类型
    GET_CHARACTER_TYPE_NAME_IS_NULL_4082(4082, "get character type name is null."),
    GET_CHARACTER_TYPE_NAME_IS_ALREADY_EXIST_4083(4083, "get character type name is already exist."),
    GET_CHARACTER_TYPE_IS_NULL_4084(4084, "get character type is null."),

    //性状名称
    GET_CHARACTER_MANAGER_NAME_IS_NULL_4085(4085, "get character manager name  is null."),
    GET_CHARACTER_MANAGER_INPUTTYPE_IS_NULL_4086(4086, "get character manager inputType  is null."),
    GET_CHARACTER_MANAGER_ADDRESS_IS_NULL_OR_LENGTH_TOO_LONG_4087(4087, "get character manager address is null or length too long."),
    GET_CHARACTER_MANAGER_IS_NULL_4088(4088,"get character manager is null"),
    GET_PLANT_RESOURCE_REASON_IS_NULL_OR_LENGTH_TOO_LONG_4089(4089,"get plant resource reason is null or length too long"),


    ;


    private final Integer code;
    private final String description;

    ExceptionDefinition(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
