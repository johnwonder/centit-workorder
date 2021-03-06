package com.centit.workorder.controller;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.basedata.IUserInfo;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.database.utils.PageDesc;
import com.centit.workorder.po.AssistOperator;
import com.centit.workorder.po.QuestionInfo;
import com.centit.workorder.service.QuestionInfoManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * QuestionInfo  Controller.
 * create by scaffold 2017-05-08
 * @author codefan@sina.com
 * 系统问题列表null
*/


@Controller
@RequestMapping("/os/{osId}/questions")
public class QuestionInfoController  extends BaseController {
    //private static final Logger logger = LoggerFactory.getLogger(HelpDocController.class);

    @Resource
    private QuestionInfoManager questionInfoMag;

    @Resource
    protected PlatformEnvironment platformEnvironment;

    /**
     * 查询所有   系统问题列表  列表  客户展示端
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return {data:[]}
     */
    @RequestMapping(method = RequestMethod.GET)
    public void list(@PathVariable String osId, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        String questionTitle = request.getParameter("questionTitle");
        String questionContent = request.getParameter("questionContent");
        String questionState = request.getParameter("questionState");
        String editState = request.getParameter("editState");
        String currentOperator = request.getParameter("currentOperator");
        Date begin = DatetimeOpt.convertStringToDate(request.getParameter("begin"),"yyyy-MM-dd");
        Date end = DatetimeOpt.convertStringToDate(request.getParameter("end"),"yyyy-MM-dd");
        map.put("osId",osId);
        if (!StringUtils.isBlank(questionTitle)){
            map.put("questionTitle","%"+questionTitle+"%");
        }
        map.put("questionContent",questionContent);
        map.put("questionState",questionState);
        map.put("currentOperator",currentOperator);
        map.put("editState",editState);
        map.put("begin",begin);
        map.put("end",end);
        map.put("ORDER_BY","createTime desc");
//        //暂不考虑分页
//        List<QuestionInfo> listObjects = questionInfoMag.listObjects(map);
//        JsonResultUtils.writeSingleDataJson(listObjects, response);
        JSONArray listObjects = questionInfoMag.getQuestionInfo(map, pageDesc);
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    /**
     * 获取问题类别下的所有问题列表
     * @param catalogId
     * @param response
     */
    @RequestMapping(value = "/{catalogId}/catalog", method = {RequestMethod.GET})
    public void getQuestionInfo(@PathVariable String catalogId, HttpServletResponse response){
        List<QuestionInfo> questionInfoList = questionInfoMag.getQuestionInfoWithCatalogId(catalogId);
        JsonResultUtils.writeSingleDataJson(questionInfoList, response);
    }

    /**
     * 根据questionID获取工单信息
     * @param questionId
     * @param response
     */
    @RequestMapping(value = "/{questionId}", method = {RequestMethod.GET})
    public void getQuestionInfoWithQuestionId(@PathVariable String questionId, HttpServletResponse response){
        QuestionInfo questionInfo = questionInfoMag.getObjectById(questionId);
        JsonResultUtils.writeSingleDataJson(questionInfo, response);
    }

    /**
     * 根据questionID获取工单信息和当前用户是责任人还是协助处理人标识
     * @param questionId
     * @param response
     */
    @RequestMapping(value = "/questionInfo/{questionId}", method = {RequestMethod.GET})
    public void questionInfo(@PathVariable String questionId,HttpServletRequest request, HttpServletResponse response){
        String role = questionInfoMag.loginRole(questionId,WebOptUtils.getCurrentUserCode(request),
                WebOptUtils.getCurrentUserName(request));
        QuestionInfo questionInfo = questionInfoMag.getObjectById(questionId);
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData("object", questionInfo);
        resData.addResponseData("role", role);
        JsonResultUtils.writeSingleDataJson(resData, response);
    }

    /**
     * 获取未分配责任人的工单列表
     * @param response
     */
    @RequestMapping(value = "/unabsorbedQuestion", method = {RequestMethod.GET})
    public void getUnabsorbedQuestionList(HttpServletResponse response){
        List<QuestionInfo> QuestionInfoList = questionInfoMag.getUnabsorbedQuestion();
        JsonResultUtils.writeSingleDataJson(QuestionInfoList, response);
    }

    /**
     * 新增 系统问题列表
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST})
    public void createQuestionInfo(HttpServletRequest request,
                                   HttpServletResponse response,
                                   @RequestBody QuestionInfo questionInfo) {
        questionInfo.setUserCode(WebOptUtils.getCurrentUserCode(request));
        questionInfo.setUserName(WebOptUtils.getCurrentUserName(request));
        questionInfoMag.createQuestion(questionInfo);
        JsonResultUtils.writeSuccessJson(response);
    }

    /**
     * 删除单个  系统问题列表
     * @param questionId  QUESTION_ID
     */
    @RequestMapping(value = "/{questionId}", method = {RequestMethod.DELETE})
    public void deleteQuestionInfo(@PathVariable String questionId, HttpServletResponse response) {
        questionInfoMag.deleteQuestion(questionId);
        JsonResultUtils.writeSingleDataJson(questionId,response);
    }

    /**
     * 修改问题
     * @param questionId  QUESTION_ID
     * @param response    {@link HttpServletResponse}
     */
    @RequestMapping(value = "/{questionId}", method = {RequestMethod.PUT})
    public void updateQuestionInfo(@PathVariable String questionId,
                                   @RequestBody QuestionInfo questionInfo,
                                   HttpServletResponse response) {
        if (questionInfo == null){
            JsonResultUtils.writeErrorMessageJson("当前对象不存在", response);
            return;
        }
        QuestionInfo dbQuestionInfo  = questionInfoMag.getObjectById( questionId);
        dbQuestionInfo.copyNotNullProperty(questionInfo);
        questionInfoMag.mergeObject(dbQuestionInfo);
        JsonResultUtils.writeSingleDataJson(questionId,response);
    }

    /**
     * 修改责任人
     * @param questionId  QUESTION_ID
     * @param response    {@link HttpServletResponse}
     */
    @RequestMapping(value = "/{questionId}/operator", method = {RequestMethod.PUT})
    public void updateOperator(@PathVariable String questionId,
                               HttpServletResponse response,
                               @RequestBody QuestionInfo questionInfo) {
        if (questionInfo == null){
            JsonResultUtils.writeErrorMessageJson("当前对象不存在", response);
            return;
        }
        QuestionInfo dbQuestionInfo  = questionInfoMag.getObjectById(questionId);
        dbQuestionInfo .setCurrentOperator(questionInfo.getCurrentOperator());
        dbQuestionInfo.setQuestionState("H");
        dbQuestionInfo.setAcceptTime(DatetimeOpt.currentUtilDate());
        questionInfoMag.mergeObject(dbQuestionInfo);
        questionInfoMag.addDefaultReplay(questionId);
        JsonResultUtils.writeSingleDataJson(questionId,response);
    }

    /**
     * 工单分配给我自己(抢单)
     * @param questionId
     * @param response
     */
    @RequestMapping(value = "/{questionId}/grab", method = {RequestMethod.PUT})
    public void allotToMyself(@PathVariable String questionId,
                              HttpServletRequest request,
                              HttpServletResponse response) {
        QuestionInfo dbQuestionInfo  = questionInfoMag.getObjectById(questionId);
        dbQuestionInfo .setCurrentOperator(WebOptUtils.getCurrentUserName(request));
        dbQuestionInfo.setQuestionState("H");
        dbQuestionInfo.setAcceptTime(DatetimeOpt.currentUtilDate());
        questionInfoMag.mergeObject(dbQuestionInfo);
        questionInfoMag.addDefaultReplay(questionId);
        JsonResultUtils.writeSingleDataJson(dbQuestionInfo,response);
    }


    /**
     * 评价并关闭工单
     * @param questionId
     * @param response
     */
    @RequestMapping(value = "/{questionId}/comment", method = {RequestMethod.PUT})
    public void evaluateQuestionInfo(@PathVariable String questionId,
                                     String evaluateScore,
                                     HttpServletResponse response){
        String retId = questionInfoMag.evaluateAndCloseQuestion(evaluateScore,questionId);
        JsonResultUtils.writeSingleDataJson(retId,response);
    }

    /**
     * 关闭工单
     * @param questionId
     * @param response
     */
    @RequestMapping(value = "/{questionId}/closeQuestion", method = {RequestMethod.PUT})
    public void closeQuestionInfo(@PathVariable String questionId, HttpServletResponse response){
        String retId = questionInfoMag.closeQuestion(questionId);
        JsonResultUtils.writeSingleDataJson(retId,response);
    }

    /**
     * 根据userCode获取工单列表
     * @param response
     */
    @RequestMapping(value = "/consumer", method = {RequestMethod.GET})
    public void listWithUserCode(@PathVariable String osId,
                                 PageDesc pageDesc,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        String editState = request.getParameter("editState");
        String questionState = request.getParameter("questionState");
        String questionTitle = request.getParameter("questionTitle");
        String currentOperator = request.getParameter("currentOperator");
        Date begin = DatetimeOpt.convertStringToDate(request.getParameter("begin"),"yyyy-MM-dd HH:mm:ss");
        Date end = DatetimeOpt.convertStringToDate(request.getParameter("end"),"yyyy-MM-dd HH:mm:ss");
        Map<String, Object> map = new HashMap<>();
        map.put("osId",osId);
        map.put("currentOperator",currentOperator);
        map.put("editState",editState);
        map.put("questionState",questionState);
        if (!StringUtils.isBlank(questionTitle)){
            map.put("questionTitle","%"+questionTitle+"%");
        }
        map.put("userCode",userCode);
        map.put("begin",begin);
        map.put("end",end);
        JSONArray listObjects = questionInfoMag.getQuestionInfo(map, pageDesc);
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    /**
     * 根据责任人获取工单列表
     * @param currentOperator
     * @param response
     */
    @RequestMapping(value = "/{currentOperator}/operator", method = {RequestMethod.GET})
    public void listWithOperator(@PathVariable String currentOperator,
                                 @PathVariable String osId,
                                 PageDesc pageDesc,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        Date begin = DatetimeOpt.convertStringToDate(request.getParameter("begin"),"yyyy-MM-dd HH:mm:ss");
        Date end = DatetimeOpt.convertStringToDate(request.getParameter("end"),"yyyy-MM-dd HH:mm:ss");
        map.put("osId",osId);
        map.put("currentOperator",currentOperator);
        map.put("begin",begin);
        map.put("end",end);
        JSONArray listObjects = questionInfoMag.getQuestionInfo(map, pageDesc);
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    /**
     * 根据协助处理人code获取工单列表
     * @param operatorCode
     * @param response
     */
    @RequestMapping(value = "/{operatorCode}/assistOperator", method = {RequestMethod.GET})
    public void listWithOperatorCode(@PathVariable String osId,
                                     @PathVariable String operatorCode,
                                     PageDesc pageDesc, HttpServletResponse response) {
        List<QuestionInfo> listObjects = questionInfoMag.getQuestionInfoWithOperator(osId,operatorCode, pageDesc);
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    /**
     * 工单维护人员所属的工单集合（责任人或协助处理人）
     * @param osId
     * @param pageDesc
     * @param request
     * @param response
     */
    @RequestMapping(value = "/questionsList", method = {RequestMethod.GET})
    public void listQuestions(@PathVariable String osId,
                     PageDesc pageDesc,
                     HttpServletRequest request,
                     HttpServletResponse response){
        Map<String, Object> map = new HashMap<>();
        map.put("osId",osId);
        map.put("operator",WebOptUtils.getCurrentUserName(request));
        map.put("operatorCode",WebOptUtils.getCurrentUserCode(request));
        JSONArray listObjects = questionInfoMag.getQuestionInfoList(map, pageDesc);
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    /**
     * 获取所有责任人
     * @param response
     */
    @RequestMapping(value = "/allOperator", method = {RequestMethod.GET})
    public void getOperator(HttpServletResponse response) {
        List<? extends IUserInfo> userInfoList =  platformEnvironment.listAllUsers();
        JsonResultUtils.writeSingleDataJson(userInfoList, response);
    }

    /**
     * 添加协助处理人员
     * @return
     */
    @RequestMapping(value = "/assistOperator",method = {RequestMethod.POST})
    public void addAssistOperator(HttpServletResponse response,
                                  String questionId,
                                  @RequestBody AssistOperator[] assistOperators) {
        List<AssistOperator> pk = questionInfoMag.createAssistOperator(questionId,assistOperators);
        JsonResultUtils.writeSingleDataJson(pk,response);
    }

    /**
     * 删除协助处理人员
     * @return
     */
    @RequestMapping(value = "/assistOperator",method = {RequestMethod.DELETE})
    public void deleteAssistOperator(HttpServletResponse response,
                                     @RequestBody AssistOperator[] assistOperators) {
        if (assistOperators == null){
            JsonResultUtils.writeErrorMessageJson(400,"当前对象不存在", response);
            return;
        }
        questionInfoMag.deleteObject(assistOperators);
        JsonResultUtils.writeSuccessJson(response);
    }


    /**
     * 根据工单ID获取当前工单的协助处理人
     * @return
     */
    @RequestMapping(value = "/assistOperator/{questionId}",method = {RequestMethod.GET})
    public void listAssistOperator(@PathVariable String questionId,HttpServletResponse response) {
        List<AssistOperator> list = questionInfoMag.listAssistOperator(questionId);
        JsonResultUtils.writeSingleDataJson(list, response);
    }
}
