package cn.finedo.sellstd_service.homeVnet;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.finedo.business.common.BossUtil;
import cn.finedo.business.openuser.common.util.JsonUtil;
import cn.finedo.common.domain.ReturnValueDomain;
import cn.finedo.common.non.NonUtil;
import cn.finedo.common.wsinteface.XmlHelper;
import cn.finedo.fsdp.server.util.BeanUtil;
import cn.finedo.sellstd_service.blocks.domain.SqAllCust;
import cn.finedo.sellstd_service.blocks.util.MemberUtil;
import cn.finedo.sellstd_service.blocks.util.ReqUrlConfig;
import cn.finedo.sellstd_service.common.pojo.SellstdUserext;
import cn.finedo.sellstd_service.homeVnet.domain.HomeVnetDomain;
import cn.finedo.sellstd_service.kdService.util.JsonHelp;
import cn.finedo.sellstd_service.tool.CommonEsbUtil;
import cn.finedo.sellstd_service.tool.CommonTool;

/**
 * 增值产品外部接口调用类
 * @author gejianmin
 *@since 20180612
 */
public class HomeVnetInterFace {
	private static Logger logger = LoggerFactory.getLogger(HomeVnetInterFace.class);

	
	/*
	 * sWebFamPrcQry
	 */
	public static ReturnValueDomain<HomeVnetDomain> sWebFamPrcQry(HashMap<String,Object> param , SellstdUserext user) {
        ReturnValueDomain<HomeVnetDomain> ret = new ReturnValueDomain<HomeVnetDomain>();
            StringBuffer XML = new StringBuffer();
            XML.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
            XML.append("<ROOT>");
            XML.append("    <PHONE_NO type=\"string\">"+param.get("phoneno")+"</PHONE_NO>");
            XML.append("    <MASTER_SERV_ID type=\"string\">1007</MASTER_SERV_ID>");
            XML.append("    <LOGIN_NO type=\"string\">"+user.getBoss_no()+"</LOGIN_NO>");
            XML.append("</ROOT>");
            XmlHelper xh=null;
            String xmlstr = "";
            try {
               String method = "sWebFamPrcQry";
               ReqUrlConfig ReqUrlConfig = BeanUtil.getBean("reqUrlConfig",ReqUrlConfig.class);
               String http_url = ReqUrlConfig.getEsbUrl();
               ReturnValueDomain<String> xml_ret =  CommonEsbUtil.sendPostRestful(http_url+method,XML.toString());
               if (xml_ret.hasFail()) {
                   return ret.setFail("调sWebFamPrcQry服务失败");
               }
               xmlstr = xml_ret.getObject();
               
               //String xmlstr = "<?xml version=\"1.0\" encoding=\"GBK\"?><ROOT><RETURN_MSG type=\"string\">OK</RETURN_MSG><PROMPT_MSG type=\"string\"/><OUT_DATA><PRODPRC_INFO><STATE type=\"string\">A</STATE><CANCEL_RULE_ID type=\"string\">1002</CANCEL_RULE_ID><MAX_NUM type=\"string\">99</MAX_NUM><TEAM_TYPE type=\"string\"/><BILLING_MODE type=\"string\">A</BILLING_MODE><MIN_NUM type=\"string\">0</MIN_NUM><GROUP_TYPE type=\"string\"/><COM_FLAG type=\"string\"/><PRICING_ID type=\"string\">0</PRICING_ID><EXP_DATE type=\"string\">20991231235959</EXP_DATE><VERSION type=\"string\">0</VERSION><PROD_ID type=\"string\">CPX006</PROD_ID> 群产品id<PROD_NAME type=\"string\">家庭包</PROD_NAME><PROD_PRCID type=\"string\">PX000006</PROD_PRCID>群资费id<PROD_PRC_TYPE type=\"string\">0</PROD_PRC_TYPE><CHINESE_INDEX type=\"string\">JTBBZZF</CHINESE_INDEX><EFF_RULE_ID type=\"string\">1001</EFF_RULE_ID><BRAND_ID type=\"string\">0  </BRAND_ID><USER_RANGE type=\"string\">10100000001</USER_RANGE><EFF_DATE type=\"string\">20090428215552</EFF_DATE><PROD_PRC_DESC type=\"string\">家庭包标准资费</PROD_PRC_DESC><PROD_TYPE type=\"string\"/><REDOFLAG type=\"string\">F</REDOFLAG><PROD_PRC_NAME type=\"string\">家庭包标准资费</PROD_PRC_NAME><EXP_RULE_ID type=\"string\">1023</EXP_RULE_ID><MODIFY_FLAG type=\"string\">00</MODIFY_FLAG><PRC_CLASS type=\"string\">0</PRC_CLASS></PRODPRC_INFO></OUT_DATA><RETURN_CODE type=\"string\">0</RETURN_CODE><USER_MSG type=\"string\">OK</USER_MSG><RUN_IP type=\"string\">10.243.8.62</RUN_IP><DETAIL_MSG type=\"string\">OK</DETAIL_MSG></ROOT>";
               xh=new XmlHelper(xmlstr);
               
            } catch (Exception e) {
               logger.error("调sWebFamPrcQry服务失败",e);
               return ret.setFail("调sWebFamPrcQry服务失败");
            }
            if (NonUtil.isNon(xh)) {
                return ret.setFail("调sWebFamPrcQry服务失败");
            }else{
                if("0".equals(xh.getNodeText("/ROOT/RETURN_CODE"))){//0 是校验通过
                    ret.setSuccess("调sWebFamPrcQry服务成功!");
                    HomeVnetDomain homeVnetDomain = new HomeVnetDomain();
                    //homeVnetDomain.setProd_id(xh.getNodeText("/ROOT/OUT_DATA/PRODPRC_INFO/PROD_ID"));
                    //homeVnetDomain.setProd_prcid(xh.getNodeText("/ROOT/OUT_DATA/PRODPRC_INFO/PROD_PRCID"));
                    Reader reader = new StringReader(xmlstr);
                    SAXReader builder = new SAXReader();
                    Document doc = null;
                    try {
                        doc = builder.read(reader);
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                    Element root = (Element) doc.getRootElement();
                    List<Element> ele = root.selectNodes("/ROOT/OUT_DATA/PRODPRC_INFO");
                    int sign = 0;
                    for (int i=0;i<ele.size();i++) {
                        for (Element element : (List<Element>) ele.get(i).elements()) {
                              if(("PROD_PRC_DESC").equals(((Element) element).getName())&&(!element.getText().contains("2成员"))){
                                  sign = i;
                                  break;
                              }
                        }
                    }
                    for (Element element : (List<Element>) ele.get(sign).elements()) {
                        if(("PROD_ID").equals(((Element) element).getName())){
                            homeVnetDomain.setProd_id(element.getText());
                        }
                        if(("PROD_PRCID").equals(((Element) element).getName())){
                            homeVnetDomain.setProd_prcid(element.getText());
                        }
                        if(("PROD_PRC_DESC").equals(((Element) element).getName())){
                            homeVnetDomain.setProd_prc_desc(element.getText());
                        }
                    }
                    ret.setObject(homeVnetDomain);
                }else{
                    ret.setFail("调sWebFamPrcQry服务失败");
                }
            }
            return ret;
    }
    /*
     * 查询成员可选资费信息
     * sProdDetailQry
     */
    public static ReturnValueDomain<Map<String,String>> sProdDetailQry(HashMap<String,Object> param , SellstdUserext user) {
        ReturnValueDomain<Map<String,String>> ret = new ReturnValueDomain<Map<String,String>>();
            StringBuffer XML = new StringBuffer();
            XML.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            XML.append("");
            XML.append("<ROOT>");
            XML.append("  <COMMON_INFO>");
            XML.append("    <TRACE_ID type=\"string\">03*20181013085857*1496*Y10086CLT*375826</TRACE_ID>");
            XML.append("    <CALL_ID type=\"string\">859e2175-d016-489d-9269-1d94c44ce278</CALL_ID>");
            XML.append("    <OP_CODE type=\"string\">1496</OP_CODE>");
            XML.append("  </COMMON_INFO>");
            XML.append("  <REQUEST_INFO>");
            XML.append("    <OPR_INFO>");
            XML.append("      <CHANNEL_TYPE type=\"string\"/>");
            XML.append("      <LOGIN_NO type=\"string\">Y10086CLT</LOGIN_NO>");
            XML.append("      <LOGIN_PWD type=\"string\"/>");
            XML.append("      <IP_ADDRESS type=\"string\"/>");
            XML.append("      <GROUP_ID type=\"string\"/>");
            XML.append("      <CONTACT_ID type=\"string\"/>");
            XML.append("      <OP_CODE type=\"string\"/>");
            XML.append("    </OPR_INFO>");
            XML.append("    <BUSI_INFO_LIST>");
            XML.append("      <BUSI_INFO>");
            XML.append("        <PROD_ID type=\"string\">"+param.get("prod_id")+"</PROD_ID>");  //sWebFamPrcQry服务查询出来的群产品id
            XML.append("      </BUSI_INFO>");
            XML.append("    </BUSI_INFO_LIST>");
            XML.append("  </REQUEST_INFO>");
            XML.append("</ROOT>");
            XmlHelper xh=null;
            try {
               String method = "sProdDetailQry";
               ReqUrlConfig ReqUrlConfig = BeanUtil.getBean("reqUrlConfig",ReqUrlConfig.class);
               String http_url = ReqUrlConfig.getEsbUrl();
               ReturnValueDomain<String> xml_ret =  CommonEsbUtil.sendPostRestful(http_url+method,XML.toString());
               if (xml_ret.hasFail()) {
                   return ret.setFail("调sProdDetailQry服务失败");
               }
               String xmlstr = xml_ret.getObject();
               
               //String xmlstr = "<?xml version=\"1.0\" encoding=\"GBK\"?><ROOT><RETURN_MSG type=\"string\">OK</RETURN_MSG><PROMPT_MSG type=\"string\"></PROMPT_MSG><OUT_DATA><PROD_DETAIL><MEMBER_ROLE_TYPE type=\"string\">D1</MEMBER_ROLE_TYPE><DOWN_AMOUNT type=\"int\">1</DOWN_AMOUNT><CANCEL_RULE_ID type=\"string\">1023</CANCEL_RULE_ID><OPERATION_FLAG type=\"string\">C</OPERATION_FLAG><TEAM_TYPE type=\"string\">0</TEAM_TYPE><MAX_MONEY type=\"int\">99999</MAX_MONEY><COM_FLAG type=\"string\">0</COM_FLAG><GROUP_TYPE type=\"string\">0</GROUP_TYPE><ROLE_ID type=\"string\">000</ROLE_ID><SEL_FLAG type=\"string\">0</SEL_FLAG><PROD_ID type=\"string\">CPX014</PROD_ID><MASTER_SERV_ID type=\"string\">1001</MASTER_SERV_ID><PROD_DESC type=\"string\">家庭计划</PROD_DESC><PROD_NAME type=\"string\">家庭计划</PROD_NAME><LEAVE_FLAG type=\"string\">N</LEAVE_FLAG><MEM_PRIORITY type=\"int\">0</MEM_PRIORITY><IS_ATTR type=\"int\">1</IS_ATTR><ELEMENT_CODE type=\"string\">J0052</ELEMENT_CODE><EFF_RULE_ID type=\"string\">1002</EFF_RULE_ID><USER_RANGE type=\"string\">0001b000000</USER_RANGE><MIN_AMOUNT type=\"int\">0</MIN_AMOUNT><ROLE_NAME type=\"string\">默认角色包 </ROLE_NAME><ELEMENT_FLAG type=\"string\">1</ELEMENT_FLAG><UP_AMOUNT type=\"int\">7</UP_AMOUNT><MAX_AMOUNT type=\"int\">999</MAX_AMOUNT><PROD_TYPE type=\"string\">1</PROD_TYPE><ORDER_DEAL_FLAG type=\"int\">0</ORDER_DEAL_FLAG><EXP_RULE_ID type=\"string\">1023</EXP_RULE_ID><LD_FLAG type=\"string\">N</LD_FLAG><ELEMENT_ID type=\"string\">1066</ELEMENT_ID><ORDERED_CNT type=\"string\">0</ORDERED_CNT><MBR_FLAG type=\"string\">T</MBR_FLAG><MIN_MONEY type=\"int\">0</MIN_MONEY><CLASS_ID type=\"string\"></CLASS_ID></PROD_DETAIL></OUT_DATA><RETURN_CODE type=\"string\">0</RETURN_CODE><USER_MSG type=\"string\">OK</USER_MSG><RUN_IP type=\"string\">10.243.8.68</RUN_IP><DETAIL_MSG type=\"string\">OK</DETAIL_MSG></ROOT>";
               xh=new XmlHelper(xmlstr);
               
            } catch (Exception e) {
               logger.error("调sProdDetailQry服务失败",e);
               return ret.setFail("调sProdDetailQry服务失败");
            }
            if (NonUtil.isNon(xh)) {
                return ret.setFail("调sProdDetailQry服务失败");
            }else{
                if("0".equals(xh.getNodeText("/ROOT/RETURN_CODE"))){//0 是校验通过
                    ret.setSuccess("调sProdDetailQry服务成功!");
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("ELEMENT_ID", xh.getNodeText("/ROOT/OUT_DATA/PROD_DETAIL/ELEMENT_ID"));
                    map.put("ELEMENT_CODE", xh.getNodeText("/ROOT/OUT_DATA/PROD_DETAIL/ELEMENT_CODE"));
                    ret.setObject(map);
                }else{
                    ret.setFail("调sProdDetailQry服务失败");
                }
            }
            return ret;
    }
    
    /*
     * 查询成员可订购套餐
     * sJTJHProdPrcQry
     */
    public static ReturnValueDomain<HomeVnetDomain> sJTJHProdPrcQry(HashMap<String,Object> param , SellstdUserext user) {
        ReturnValueDomain<HomeVnetDomain> ret = new ReturnValueDomain<HomeVnetDomain>();
            StringBuffer XML = new StringBuffer();
            XML.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            XML.append("<ROOT>");
            XML.append("  <REQUEST_INFO>");
            XML.append("    <OPR_INFO>");
            XML.append("      <REGION_ID type=\"int\"/>");
            XML.append("      <CHANNEL_TYPE type=\"string\"/>");
            XML.append("      <LOGIN_NO type=\"string\">Y10086CLT</LOGIN_NO>");
            XML.append("      <LOGIN_PWD type=\"string\"/>");
            XML.append("      <IP_ADDRESS type=\"string\"/>");
            XML.append("      <GROUP_ID type=\"string\"/>");
            XML.append("      <CONTACT_ID type=\"string\"/>");
            XML.append("      <OP_CODE type=\"string\"/>");
            XML.append("      <PHONE_NO type=\"string\">"+param.get("phoneno")+"</PHONE_NO>");  //主角号码
            XML.append("      <RELEASEWT_FLAG type=\"string\">1</RELEASEWT_FLAG>");
            XML.append("    </OPR_INFO>");
            XML.append("    <BUSI_INFO_LIST>");
            XML.append("      <BUSI_INFO>");
            XML.append("        <FLAG type=\"string\">2</FLAG>");
            XML.append("        <OPERATION_FLAG type=\"string\">A</OPERATION_FLAG>");
            XML.append("        <PAR_PROD_ID type=\"string\">"+param.get("par_prod_id")+"</PAR_PROD_ID>");  //群产品id
            XML.append("        <PAR_PRC_ID type=\"string\">"+param.get("par_prc_id")+"</PAR_PRC_ID>"); //群资费id
            XML.append("        <PROD_DETAIL>");
            XML.append("          <ELEMENT_ID type=\"string\">"+param.get("element_id")+"</ELEMENT_ID>");   //sProdDetailQry服务出参ELEMENT_ID
            XML.append("          <PROD_ID type=\"string\">"+param.get("element_code")+"</PROD_ID>");   //服务出参ELEMENT_CODE
            XML.append("        </PROD_DETAIL>");
            XML.append("      </BUSI_INFO>");
            XML.append("    </BUSI_INFO_LIST>");
            XML.append("  </REQUEST_INFO>");
            XML.append("</ROOT>");
            XmlHelper xh=null;
            try {
               String method = "sJTJHProdPrcQry";
               ReqUrlConfig ReqUrlConfig = BeanUtil.getBean("reqUrlConfig",ReqUrlConfig.class);
               String http_url = ReqUrlConfig.getEsbUrl();
               ReturnValueDomain<String> xml_ret =  CommonEsbUtil.sendPostRestful(http_url+method,XML.toString());
               if (xml_ret.hasFail()) {
                   return ret.setFail("调sJTJHProdPrcQry服务失败");
               }
               String xmlstr = xml_ret.getObject();
               
               //String xmlstr = "<?xml version=\"1.0\" encoding=\"GBK\"?><ROOT><RETURN_MSG type=\"string\">ok!</RETURN_MSG><PROMPT_MSG type=\"string\"></PROMPT_MSG><OUT_DATA><PRC_LIST><PROD_ID type=\"string\">J0052</PROD_ID><DATA><STATE type=\"string\">A</STATE><CANCEL_RULE_ID type=\"string\">1002</CANCEL_RULE_ID><OPERATION_FLAG type=\"string\">C</OPERATION_FLAG><MULTI_REL type=\"string\">N</MULTI_REL><SEND_DESC type=\"string\"></SEND_DESC><OTHER_DESC type=\"string\"></OTHER_DESC><BILLING_MODE type=\"string\">A</BILLING_MODE><FLOW_UNIT type=\"string\"></FLOW_UNIT><COM_FLAG type=\"string\"></COM_FLAG><EXP_DATE type=\"string\">20991231235959</EXP_DATE><SEL_FLAG type=\"string\">0</SEL_FLAG><SUM_FEE type=\"int\">0</SUM_FEE><CANCEL_DATE type=\"string\">20181031235959</CANCEL_DATE><PROD_ID type=\"string\">J0052</PROD_ID><EFF_DATE_FLAG type=\"string\">0</EFF_DATE_FLAG><ATTR_IDLIST type=\"string\">SVC_SEP|IsShare</ATTR_IDLIST><PRODPRC_FEE/><OLD_EXP_DATE type=\"string\">20181013092426</OLD_EXP_DATE><PROD_PRCID type=\"string\">PR060604</PROD_PRCID><PROD_PRC_TYPE type=\"string\">1</PROD_PRC_TYPE><CHINESE_INDEX type=\"string\">JTW3YTCB300FZ</CHINESE_INDEX><FEE_UNIT type=\"string\"></FEE_UNIT><BASE_TIME_TYPE type=\"string\"></BASE_TIME_TYPE><OUTSIDE_DESC type=\"string\"></OUTSIDE_DESC><EFF_RULE_ID type=\"string\">1001</EFF_RULE_ID><SMS_UNIT type=\"string\"></SMS_UNIT><BRAND_ID type=\"string\">0</BRAND_ID><USER_RANGE type=\"string\">0000000000</USER_RANGE><EFF_DATE type=\"string\">20181013092427</EFF_DATE><PROD_PRC_DESC type=\"string\">每人月费3元，每人本地拨打家庭成员基本通话300分钟免费（包含V网分钟数），本地接听成员电话免费，当月申请当月生效。</PROD_PRC_DESC><DEVRIT_PRC/><EXP_DATE_FLAG type=\"string\">0</EXP_DATE_FLAG><ATTR_FLAG type=\"string\">Y</ATTR_FLAG><PRCATTR_LIMT_LIST/><PROD_PRC_NAME type=\"string\">家庭网3元套餐（包300分钟）</PROD_PRC_NAME><REDOFLAG type=\"string\">F</REDOFLAG><EXP_RULE_ID type=\"string\">1023</EXP_RULE_ID><MODIFY_FLAG type=\"string\">00</MODIFY_FLAG><PRC_CLASS type=\"string\">0</PRC_CLASS><CALL_UNIT type=\"string\"></CALL_UNIT></DATA><DATA><STATE type=\"string\">A</STATE><CANCEL_RULE_ID type=\"string\">1002</CANCEL_RULE_ID><OPERATION_FLAG type=\"string\">C</OPERATION_FLAG><MULTI_REL type=\"string\">N</MULTI_REL><SEND_DESC type=\"string\"></SEND_DESC><OTHER_DESC type=\"string\"></OTHER_DESC><BILLING_MODE type=\"string\">A</BILLING_MODE><FLOW_UNIT type=\"string\"></FLOW_UNIT><COM_FLAG type=\"string\"></COM_FLAG><EXP_DATE type=\"string\">20991231235959</EXP_DATE><SEL_FLAG type=\"string\">0</SEL_FLAG><SUM_FEE type=\"int\">0</SUM_FEE><CANCEL_DATE type=\"string\">20181031235959</CANCEL_DATE><PROD_ID type=\"string\">J0052</PROD_ID><EFF_DATE_FLAG type=\"string\">0</EFF_DATE_FLAG><ATTR_IDLIST type=\"string\">SVC_SEP|IsShare</ATTR_IDLIST><PRODPRC_FEE/><OLD_EXP_DATE type=\"string\">20181013092426</OLD_EXP_DATE><PROD_PRCID type=\"string\">PR060231</PROD_PRCID><PROD_PRC_TYPE type=\"string\">1</PROD_PRC_TYPE><CHINESE_INDEX type=\"string\">JTJH1YTC(B50FZ)</CHINESE_INDEX><FEE_UNIT type=\"string\"></FEE_UNIT><BASE_TIME_TYPE type=\"string\"></BASE_TIME_TYPE><OUTSIDE_DESC type=\"string\"></OUTSIDE_DESC><EFF_RULE_ID type=\"string\">1001</EFF_RULE_ID><SMS_UNIT type=\"string\"></SMS_UNIT><BRAND_ID type=\"string\">0</BRAND_ID><USER_RANGE type=\"string\">1110000000100</USER_RANGE><EFF_DATE type=\"string\">20181013092427</EFF_DATE><PROD_PRC_DESC type=\"string\">每人月费1元，每人本地拨打家庭成员基本通话50分钟免费（包含V网分钟数），本地接听成员电话免费，当月申请当月生效。</PROD_PRC_DESC><DEVRIT_PRC/><EXP_DATE_FLAG type=\"string\">0</EXP_DATE_FLAG><ATTR_FLAG type=\"string\">Y</ATTR_FLAG><PRCATTR_LIMT_LIST/><PROD_PRC_NAME type=\"string\">家庭计划1元套餐（包50分钟）</PROD_PRC_NAME><REDOFLAG type=\"string\">F</REDOFLAG><EXP_RULE_ID type=\"string\">1023</EXP_RULE_ID><MODIFY_FLAG type=\"string\">00</MODIFY_FLAG><PRC_CLASS type=\"string\">0</PRC_CLASS><CALL_UNIT type=\"string\"></CALL_UNIT></DATA><DATA><STATE type=\"string\">A</STATE><CANCEL_RULE_ID type=\"string\">1002</CANCEL_RULE_ID><OPERATION_FLAG type=\"string\">C</OPERATION_FLAG><MULTI_REL type=\"string\">N</MULTI_REL><SEND_DESC type=\"string\"></SEND_DESC><OTHER_DESC type=\"string\"></OTHER_DESC><BILLING_MODE type=\"string\">A</BILLING_MODE><FLOW_UNIT type=\"string\"></FLOW_UNIT><COM_FLAG type=\"string\"></COM_FLAG><EXP_DATE type=\"string\">20991231235959</EXP_DATE><SEL_FLAG type=\"string\">0</SEL_FLAG><SUM_FEE type=\"int\">0</SUM_FEE><CANCEL_DATE type=\"string\">20181031235959</CANCEL_DATE><PROD_ID type=\"string\">J0052</PROD_ID><EFF_DATE_FLAG type=\"string\">0</EFF_DATE_FLAG><ATTR_IDLIST type=\"string\">SVC_SEP|IsShare</ATTR_IDLIST><PRODPRC_FEE/><OLD_EXP_DATE type=\"string\">20181013092426</OLD_EXP_DATE><PROD_PRCID type=\"string\">PR060239</PROD_PRCID><PROD_PRC_TYPE type=\"string\">1</PROD_PRC_TYPE><CHINESE_INDEX type=\"string\">JTJH2YTC(B100FZ)</CHINESE_INDEX><FEE_UNIT type=\"string\"></FEE_UNIT><BASE_TIME_TYPE type=\"string\"></BASE_TIME_TYPE><OUTSIDE_DESC type=\"string\"></OUTSIDE_DESC><EFF_RULE_ID type=\"string\">1001</EFF_RULE_ID><SMS_UNIT type=\"string\"></SMS_UNIT><BRAND_ID type=\"string\">0</BRAND_ID><USER_RANGE type=\"string\">1110000000100</USER_RANGE><EFF_DATE type=\"string\">20181013092427</EFF_DATE><PROD_PRC_DESC type=\"string\">每人月费2元，每人本地拨打家庭成员基本通话100分钟免费（包含V网分钟数），本地接听成员电话免费，当月申请当月生效。</PROD_PRC_DESC><DEVRIT_PRC/><EXP_DATE_FLAG type=\"string\">0</EXP_DATE_FLAG><ATTR_FLAG type=\"string\">Y</ATTR_FLAG><PRCATTR_LIMT_LIST/><PROD_PRC_NAME type=\"string\">家庭计划2元套餐(包100分钟)</PROD_PRC_NAME><REDOFLAG type=\"string\">F</REDOFLAG><EXP_RULE_ID type=\"string\">1023</EXP_RULE_ID><MODIFY_FLAG type=\"string\">00</MODIFY_FLAG><PRC_CLASS type=\"string\">0</PRC_CLASS><CALL_UNIT type=\"string\"></CALL_UNIT></DATA><DATA><STATE type=\"string\">A</STATE><CANCEL_RULE_ID type=\"string\">1002</CANCEL_RULE_ID><OPERATION_FLAG type=\"string\">C</OPERATION_FLAG><MULTI_REL type=\"string\">N</MULTI_REL><SEND_DESC type=\"string\"></SEND_DESC><OTHER_DESC type=\"string\"></OTHER_DESC><BILLING_MODE type=\"string\">A</BILLING_MODE><FLOW_UNIT type=\"string\"></FLOW_UNIT><COM_FLAG type=\"string\"></COM_FLAG><EXP_DATE type=\"string\">20190930235959</EXP_DATE><SEL_FLAG type=\"string\">0</SEL_FLAG><SUM_FEE type=\"int\">0</SUM_FEE><CANCEL_DATE type=\"string\">20181031235959</CANCEL_DATE><PROD_ID type=\"string\">J0052</PROD_ID><EFF_DATE_FLAG type=\"string\">0</EFF_DATE_FLAG><ATTR_IDLIST type=\"string\">SVC_SEP|IsShare</ATTR_IDLIST><PRODPRC_FEE/><OLD_EXP_DATE type=\"string\">20181013092426</OLD_EXP_DATE><PROD_PRCID type=\"string\">PR060286</PROD_PRCID><PROD_PRC_TYPE type=\"string\">1</PROD_PRC_TYPE><CHINESE_INDEX type=\"string\">JTJH0YTC(B100FZ12GY)</CHINESE_INDEX><FEE_UNIT type=\"string\"></FEE_UNIT><BASE_TIME_TYPE type=\"string\"></BASE_TIME_TYPE><OUTSIDE_DESC type=\"string\"></OUTSIDE_DESC><EFF_RULE_ID type=\"string\">1001</EFF_RULE_ID><SMS_UNIT type=\"string\"></SMS_UNIT><BRAND_ID type=\"string\">0</BRAND_ID><USER_RANGE type=\"string\">1110000000100</USER_RANGE><EFF_DATE type=\"string\">20181013092427</EFF_DATE><PROD_PRC_DESC type=\"string\">无月固定费用，每人本地拨打家庭成员基本通话100分钟免费（包含V网分钟数），本地接听成员电话免费，当月申请当月生效。有效期12个月（包括办理当月）。</PROD_PRC_DESC><DEVRIT_PRC/><EXP_DATE_FLAG type=\"string\">0</EXP_DATE_FLAG><ATTR_FLAG type=\"string\">Y</ATTR_FLAG><PRCATTR_LIMT_LIST/><PROD_PRC_NAME type=\"string\">家庭计划0元套餐(包100分钟，12个月)</PROD_PRC_NAME><REDOFLAG type=\"string\">F</REDOFLAG><EXP_RULE_ID type=\"string\">1007</EXP_RULE_ID><MODIFY_FLAG type=\"string\">00</MODIFY_FLAG><PRC_CLASS type=\"string\">0</PRC_CLASS><CALL_UNIT type=\"string\"></CALL_UNIT></DATA><DATA><STATE type=\"string\">A</STATE><CANCEL_RULE_ID type=\"string\">1002</CANCEL_RULE_ID><OPERATION_FLAG type=\"string\">C</OPERATION_FLAG><MULTI_REL type=\"string\">N</MULTI_REL><SEND_DESC type=\"string\"></SEND_DESC><OTHER_DESC type=\"string\"></OTHER_DESC><BILLING_MODE type=\"string\">A</BILLING_MODE><FLOW_UNIT type=\"string\"></FLOW_UNIT><COM_FLAG type=\"string\"></COM_FLAG><EXP_DATE type=\"string\">20190930235959</EXP_DATE><SEL_FLAG type=\"string\">0</SEL_FLAG><SUM_FEE type=\"int\">0</SUM_FEE><CANCEL_DATE type=\"string\">20181031235959</CANCEL_DATE><PROD_ID type=\"string\">J0052</PROD_ID><EFF_DATE_FLAG type=\"string\">0</EFF_DATE_FLAG><ATTR_IDLIST type=\"string\">SVC_SEP|IsShare</ATTR_IDLIST><PRODPRC_FEE/><OLD_EXP_DATE type=\"string\">20181013092426</OLD_EXP_DATE><PROD_PRCID type=\"string\">PR060287</PROD_PRCID><PROD_PRC_TYPE type=\"string\">1</PROD_PRC_TYPE><CHINESE_INDEX type=\"string\">JTJH0YTC(B200FZ12GY)</CHINESE_INDEX><FEE_UNIT type=\"string\"></FEE_UNIT><BASE_TIME_TYPE type=\"string\"></BASE_TIME_TYPE><OUTSIDE_DESC type=\"string\"></OUTSIDE_DESC><EFF_RULE_ID type=\"string\">1001</EFF_RULE_ID><SMS_UNIT type=\"string\"></SMS_UNIT><BRAND_ID type=\"string\">0</BRAND_ID><USER_RANGE type=\"string\">1110000000100</USER_RANGE><EFF_DATE type=\"string\">20181013092427</EFF_DATE><PROD_PRC_DESC type=\"string\">无月固定费用，每人本地拨打家庭成员基本通话200分钟免费（包含V网分钟数），本地接听成员电话免费，当月申请当月生效。有效期12个月（包括办理当月）。</PROD_PRC_DESC><DEVRIT_PRC/><EXP_DATE_FLAG type=\"string\">0</EXP_DATE_FLAG><ATTR_FLAG type=\"string\">Y</ATTR_FLAG><PRCATTR_LIMT_LIST/><PROD_PRC_NAME type=\"string\">家庭计划0元套餐(包200分钟，12个月)</PROD_PRC_NAME><REDOFLAG type=\"string\">F</REDOFLAG><EXP_RULE_ID type=\"string\">1007</EXP_RULE_ID><MODIFY_FLAG type=\"string\">00</MODIFY_FLAG><PRC_CLASS type=\"string\">0</PRC_CLASS><CALL_UNIT type=\"string\"></CALL_UNIT></DATA><DATA><STATE type=\"string\">A</STATE><CANCEL_RULE_ID type=\"string\">1002</CANCEL_RULE_ID><OPERATION_FLAG type=\"string\">C</OPERATION_FLAG><MULTI_REL type=\"string\">N</MULTI_REL><SEND_DESC type=\"string\"></SEND_DESC><OTHER_DESC type=\"string\"></OTHER_DESC><BILLING_MODE type=\"string\">A</BILLING_MODE><FLOW_UNIT type=\"string\"></FLOW_UNIT><COM_FLAG type=\"string\"></COM_FLAG><EXP_DATE type=\"string\">20991231235959</EXP_DATE><SEL_FLAG type=\"string\">0</SEL_FLAG><SUM_FEE type=\"int\">0</SUM_FEE><CANCEL_DATE type=\"string\">20181031235959</CANCEL_DATE><PROD_ID type=\"string\">J0052</PROD_ID><EFF_DATE_FLAG type=\"string\">0</EFF_DATE_FLAG><ATTR_IDLIST type=\"string\">SVC_SEP</ATTR_IDLIST><PRODPRC_FEE/><OLD_EXP_DATE type=\"string\">20181013092426</OLD_EXP_DATE><PROD_PRCID type=\"string\">PR060587</PROD_PRCID><PROD_PRC_TYPE type=\"string\">1</PROD_PRC_TYPE><CHINESE_INDEX type=\"string\">JTJH0YB</CHINESE_INDEX><FEE_UNIT type=\"string\"></FEE_UNIT><BASE_TIME_TYPE type=\"string\"></BASE_TIME_TYPE><OUTSIDE_DESC type=\"string\"></OUTSIDE_DESC><EFF_RULE_ID type=\"string\">1001</EFF_RULE_ID><SMS_UNIT type=\"string\"></SMS_UNIT><BRAND_ID type=\"string\">0</BRAND_ID><USER_RANGE type=\"string\">0000000000</USER_RANGE><EFF_DATE type=\"string\">20181013092427</EFF_DATE><PROD_PRC_DESC type=\"string\">每人月费0元，不赠送通话时长,当月申请当月生效。</PROD_PRC_DESC><DEVRIT_PRC/><EXP_DATE_FLAG type=\"string\">0</EXP_DATE_FLAG><ATTR_FLAG type=\"string\">Y</ATTR_FLAG><PRCATTR_LIMT_LIST/><PROD_PRC_NAME type=\"string\">家庭计划0元包</PROD_PRC_NAME><REDOFLAG type=\"string\">F</REDOFLAG><EXP_RULE_ID type=\"string\">1023</EXP_RULE_ID><MODIFY_FLAG type=\"string\">00</MODIFY_FLAG><PRC_CLASS type=\"string\">0</PRC_CLASS><CALL_UNIT type=\"string\"></CALL_UNIT></DATA><DATA><STATE type=\"string\">A</STATE><CANCEL_RULE_ID type=\"string\">1002</CANCEL_RULE_ID><OPERATION_FLAG type=\"string\">C</OPERATION_FLAG><MULTI_REL type=\"string\">N</MULTI_REL><SEND_DESC type=\"string\"></SEND_DESC><OTHER_DESC type=\"string\"></OTHER_DESC><BILLING_MODE type=\"string\">A</BILLING_MODE><FLOW_UNIT type=\"string\"></FLOW_UNIT><COM_FLAG type=\"string\"></COM_FLAG><EXP_DATE type=\"string\">20991231235959</EXP_DATE><SEL_FLAG type=\"string\">0</SEL_FLAG><SUM_FEE type=\"int\">0</SUM_FEE><CANCEL_DATE type=\"string\">20181031235959</CANCEL_DATE><PROD_ID type=\"string\">J0052</PROD_ID><EFF_DATE_FLAG type=\"string\">0</EFF_DATE_FLAG><ATTR_IDLIST type=\"string\">SVC_SEP|IsShare</ATTR_IDLIST><PRODPRC_FEE/><OLD_EXP_DATE type=\"string\">20181013092426</OLD_EXP_DATE><PROD_PRCID type=\"string\">PR060595</PROD_PRCID><PROD_PRC_TYPE type=\"string\">1</PROD_PRC_TYPE><CHINESE_INDEX type=\"string\">JTW5YTCB500FZ</CHINESE_INDEX><FEE_UNIT type=\"string\"></FEE_UNIT><BASE_TIME_TYPE type=\"string\"></BASE_TIME_TYPE><OUTSIDE_DESC type=\"string\"></OUTSIDE_DESC><EFF_RULE_ID type=\"string\">1001</EFF_RULE_ID><SMS_UNIT type=\"string\"></SMS_UNIT><BRAND_ID type=\"string\">0</BRAND_ID><USER_RANGE type=\"string\">0000000000</USER_RANGE><EFF_DATE type=\"string\">20181013092427</EFF_DATE><PROD_PRC_DESC type=\"string\">每人月费5元，每人本地拨打家庭成员基本通话500分钟免费（包含V网分钟数），本地接听成员电话免费，当月申请当月生效。</PROD_PRC_DESC><DEVRIT_PRC/><EXP_DATE_FLAG type=\"string\">0</EXP_DATE_FLAG><ATTR_FLAG type=\"string\">Y</ATTR_FLAG><PRCATTR_LIMT_LIST/><PROD_PRC_NAME type=\"string\">家庭网5元套餐（包500分钟）</PROD_PRC_NAME><REDOFLAG type=\"string\">F</REDOFLAG><EXP_RULE_ID type=\"string\">1023</EXP_RULE_ID><MODIFY_FLAG type=\"string\">00</MODIFY_FLAG><PRC_CLASS type=\"string\">0</PRC_CLASS><CALL_UNIT type=\"string\"></CALL_UNIT></DATA><DATA><STATE type=\"string\">A</STATE><CANCEL_RULE_ID type=\"string\">1002</CANCEL_RULE_ID><OPERATION_FLAG type=\"string\">C</OPERATION_FLAG><MULTI_REL type=\"string\">N</MULTI_REL><SEND_DESC type=\"string\"></SEND_DESC><OTHER_DESC type=\"string\"></OTHER_DESC><BILLING_MODE type=\"string\">A</BILLING_MODE><FLOW_UNIT type=\"string\"></FLOW_UNIT><COM_FLAG type=\"string\"></COM_FLAG><EXP_DATE type=\"string\">20991231235959</EXP_DATE><SEL_FLAG type=\"string\">0</SEL_FLAG><SUM_FEE type=\"int\">0</SUM_FEE><CANCEL_DATE type=\"string\">20181031235959</CANCEL_DATE><PROD_ID type=\"string\">J0052</PROD_ID><EFF_DATE_FLAG type=\"string\">0</EFF_DATE_FLAG><ATTR_IDLIST type=\"string\">SVC_SEP|IsShare</ATTR_IDLIST><PRODPRC_FEE/><OLD_EXP_DATE type=\"string\">20181013092426</OLD_EXP_DATE><PROD_PRCID type=\"string\">PR061542</PROD_PRCID><PROD_PRC_TYPE type=\"string\">1</PROD_PRC_TYPE><CHINESE_INDEX type=\"string\">JTJH0YB(XB)</CHINESE_INDEX><FEE_UNIT type=\"string\"></FEE_UNIT><BASE_TIME_TYPE type=\"string\"></BASE_TIME_TYPE><OUTSIDE_DESC type=\"string\"></OUTSIDE_DESC><EFF_RULE_ID type=\"string\">1001</EFF_RULE_ID><SMS_UNIT type=\"string\"></SMS_UNIT><BRAND_ID type=\"string\">0</BRAND_ID><USER_RANGE type=\"string\">1110000000100</USER_RANGE><EFF_DATE type=\"string\">20181013092427</EFF_DATE><PROD_PRC_DESC type=\"string\">月费0元，本地拨打家庭成员通话0.05元/分钟,被叫免费</PROD_PRC_DESC><DEVRIT_PRC/><EXP_DATE_FLAG type=\"string\">0</EXP_DATE_FLAG><ATTR_FLAG type=\"string\">Y</ATTR_FLAG><PRCATTR_LIMT_LIST/><PROD_PRC_NAME type=\"string\">家庭计划0元包（新版）</PROD_PRC_NAME><REDOFLAG type=\"string\">F</REDOFLAG><EXP_RULE_ID type=\"string\">1023</EXP_RULE_ID><MODIFY_FLAG type=\"string\">00</MODIFY_FLAG><PRC_CLASS type=\"string\">0</PRC_CLASS><CALL_UNIT type=\"string\"></CALL_UNIT></DATA><DATA><STATE type=\"string\">A</STATE><CANCEL_RULE_ID type=\"string\">1002</CANCEL_RULE_ID><OPERATION_FLAG type=\"string\">C</OPERATION_FLAG><MULTI_REL type=\"string\">N</MULTI_REL><SEND_DESC type=\"string\"></SEND_DESC><OTHER_DESC type=\"string\"></OTHER_DESC><BILLING_MODE type=\"string\">A</BILLING_MODE><FLOW_UNIT type=\"string\"></FLOW_UNIT><COM_FLAG type=\"string\"></COM_FLAG><EXP_DATE type=\"string\">20991231235959</EXP_DATE><SEL_FLAG type=\"string\">0</SEL_FLAG><SUM_FEE type=\"int\">0</SUM_FEE><CANCEL_DATE type=\"string\">20181031235959</CANCEL_DATE><PROD_ID type=\"string\">J0052</PROD_ID><EFF_DATE_FLAG type=\"string\">0</EFF_DATE_FLAG><ATTR_IDLIST type=\"string\">SVC_SEP|IsShare</ATTR_IDLIST><PRODPRC_FEE/><OLD_EXP_DATE type=\"string\">20181013092426</OLD_EXP_DATE><PROD_PRCID type=\"string\">PR060240</PROD_PRCID><PROD_PRC_TYPE type=\"string\">1</PROD_PRC_TYPE><CHINESE_INDEX type=\"string\">JTJH2YTC(B200FZ)</CHINESE_INDEX><FEE_UNIT type=\"string\"></FEE_UNIT><BASE_TIME_TYPE type=\"string\"></BASE_TIME_TYPE><OUTSIDE_DESC type=\"string\"></OUTSIDE_DESC><EFF_RULE_ID type=\"string\">1001</EFF_RULE_ID><SMS_UNIT type=\"string\"></SMS_UNIT><BRAND_ID type=\"string\">0</BRAND_ID><USER_RANGE type=\"string\">1110000000100</USER_RANGE><EFF_DATE type=\"string\">20181013092427</EFF_DATE><PROD_PRC_DESC type=\"string\">家庭计划2元套餐 每人月费2元，每人本地拨打家庭成员基本通话200分钟免费（包含V网分钟数），本地接听成员电话免费，当月申请当月生效。</PROD_PRC_DESC><DEVRIT_PRC/><EXP_DATE_FLAG type=\"string\">0</EXP_DATE_FLAG><ATTR_FLAG type=\"string\">Y</ATTR_FLAG><PRCATTR_LIMT_LIST/><PROD_PRC_NAME type=\"string\">家庭计划2元套餐(包200分钟)</PROD_PRC_NAME><REDOFLAG type=\"string\">F</REDOFLAG><EXP_RULE_ID type=\"string\">1023</EXP_RULE_ID><MODIFY_FLAG type=\"string\">00</MODIFY_FLAG><PRC_CLASS type=\"string\">0</PRC_CLASS><CALL_UNIT type=\"string\"></CALL_UNIT></DATA><DATA><STATE type=\"string\">A</STATE><CANCEL_RULE_ID type=\"string\">1002</CANCEL_RULE_ID><OPERATION_FLAG type=\"string\">C</OPERATION_FLAG><MULTI_REL type=\"string\">N</MULTI_REL><SEND_DESC type=\"string\"></SEND_DESC><OTHER_DESC type=\"string\"></OTHER_DESC><BILLING_MODE type=\"string\">A</BILLING_MODE><FLOW_UNIT type=\"string\"></FLOW_UNIT><COM_FLAG type=\"string\"></COM_FLAG><EXP_DATE type=\"string\">20991231235959</EXP_DATE><SEL_FLAG type=\"string\">0</SEL_FLAG><SUM_FEE type=\"int\">0</SUM_FEE><CANCEL_DATE type=\"string\">20181031235959</CANCEL_DATE><PROD_ID type=\"string\">J0052</PROD_ID><EFF_DATE_FLAG type=\"string\">0</EFF_DATE_FLAG><ATTR_IDLIST type=\"string\">SVC_SEP|IsShare</ATTR_IDLIST><PRODPRC_FEE/><OLD_EXP_DATE type=\"string\">20181013092426</OLD_EXP_DATE><PROD_PRCID type=\"string\">PR060276</PROD_PRCID><PROD_PRC_TYPE type=\"string\">1</PROD_PRC_TYPE><CHINESE_INDEX type=\"string\">JTJHTC1Y(B100FZ)</CHINESE_INDEX><FEE_UNIT type=\"string\"></FEE_UNIT><BASE_TIME_TYPE type=\"string\"></BASE_TIME_TYPE><OUTSIDE_DESC type=\"string\"></OUTSIDE_DESC><EFF_RULE_ID type=\"string\">1001</EFF_RULE_ID><SMS_UNIT type=\"string\"></SMS_UNIT><BRAND_ID type=\"string\">0</BRAND_ID><USER_RANGE type=\"string\">1110000000100</USER_RANGE><EFF_DATE type=\"string\">20181013092427</EFF_DATE><PROD_PRC_DESC type=\"string\">每人月费1元，本地拨打家庭成员前100分钟免费（包含V网分钟数），本地接听成员电话免费，当月申请当月生效。</PROD_PRC_DESC><DEVRIT_PRC/><EXP_DATE_FLAG type=\"string\">0</EXP_DATE_FLAG><ATTR_FLAG type=\"string\">Y</ATTR_FLAG><PRCATTR_LIMT_LIST/><PROD_PRC_NAME type=\"string\">家庭计划套餐1元（包100分钟）</PROD_PRC_NAME><REDOFLAG type=\"string\">F</REDOFLAG><EXP_RULE_ID type=\"string\">1023</EXP_RULE_ID><MODIFY_FLAG type=\"string\">00</MODIFY_FLAG><PRC_CLASS type=\"string\">0</PRC_CLASS><CALL_UNIT type=\"string\"></CALL_UNIT></DATA></PRC_LIST></OUT_DATA><RETURN_CODE type=\"string\">0</RETURN_CODE><USER_MSG type=\"string\">OK</USER_MSG><RUN_IP type=\"string\">10.243.8.68</RUN_IP><DETAIL_MSG type=\"string\">OK</DETAIL_MSG></ROOT>";
               xh=new XmlHelper(xmlstr);
               
            } catch (Exception e) {
               logger.error("调sJTJHProdPrcQry服务失败",e);
               return ret.setFail("调sJTJHProdPrcQry服务失败");
            }
            if (NonUtil.isNon(xh)) {
                return ret.setFail("调sJTJHProdPrcQry服务失败");
            }else{
                if("0".equals(xh.getNodeText("/ROOT/RETURN_CODE"))){//0 是校验通过
                    ret.setSuccess("调sJTJHProdPrcQry服务成功!");
                    HomeVnetDomain homeVnetDomain = new HomeVnetDomain();
                    homeVnetDomain.setProd_prcid(xh.getNodeText("/ROOT/OUT_DATA/PRC_LIST/DATA/PROD_PRCID"));
                    homeVnetDomain.setProd_prc_name(xh.getNodeText("/ROOT/OUT_DATA/PRC_LIST/DATA/PROD_PRC_NAME"));
                    ret.setObject(homeVnetDomain);
                }else{
                    ret.setFail("调sJTJHProdPrcQry服务失败");
                }
            }
            return ret;
    }
    	
    
    /**
     * sGPQ_MbrPrcQry
     * 查询群资费支持的成员资费
     */
    public static ReturnValueDomain<JSONObject> sGPQ_MbrPrcQry(Map map , SellstdUserext u){
        ReturnValueDomain<JSONObject> ret=new ReturnValueDomain<JSONObject>();
        Map<String,Object> param=new HashMap<String,Object>();
        Map<String,Object> root=new HashMap<String,Object>();
        Object common_info=CommonTool.getCommon_info("1486",u.getBoss_no() );
        Object opr_info=CommonTool.getOpr_info(u.getChanneltype(), u.getGroup_id(), u.getBoss_no(), "1486", u.getRegin_id());
        Map<String,Object> request_info=new HashMap<String,Object>();
        Map<String,Object> busi_info_list=new HashMap<String,Object>();
        Map<String,Object> busi_info=new HashMap<String,Object>();
    
        busi_info.put("FLAG", "2");
        busi_info.put("MASTER_SERV_ID", "1001");
        busi_info.put("PHONE_NO", map.get("phoneno"));
        busi_info.put("PROD_ID", map.get("prod_id"));
        busi_info.put("PROD_PRCID", map.get("prod_prcid"));
        
        
        
        busi_info_list.put("BUSI_INFO", busi_info);
        request_info.put("COMMON_INFO", common_info);
        request_info.put("OPR_INFO", opr_info);
        request_info.put("BUSI_INFO_LIST", busi_info_list);
        root.put("REQUEST_INFO", request_info);
        param.put("ROOT", root);
        String paramstr= JSONObject.toJSONString(param);
        ReqUrlConfig ReqUrlConfig=BeanUtil.getBean("reqUrlConfig", ReqUrlConfig.class);
        String http_url=ReqUrlConfig.getEsbUrl();
        String method = "sGPQ_MbrPrcQry";
        JSONObject jsonObject=null;
        try{
             ReturnValueDomain<String> esb_ret =  CommonEsbUtil.sendPostRestful(http_url+method,paramstr);
             if(esb_ret.hasFail()||NonUtil.isNon(esb_ret.getObject())){
                 return ret.setFail("资费校验失败");
             }
             String json = esb_ret.getObject();
             //test多个资费
             //String json = "{\"ROOT\":{\"DETAIL_MSG\":\"OK\",\"OUT_DATA\":{\"DOWN_AMOUNT\":1,\"PRC_LIST\":{\"DATA\":[{\"ATTR_FLAG\":\"Y\",\"ATTR_IDLIST\":\"SVC_SEP|IsShare\",\"BILLING_MODE\":\"A\",\"BRAND_ID\":\"0\",\"CALL_UNIT\":\"分钟\",\"CANCEL_DATE\":\"20190131235959\",\"CANCEL_RULE_ID\":\"1002\",\"CHINESE_INDEX\":\"JTJH1YTC(B50FZ)\",\"COM_FLAG\":\"0\",\"EFF_DATE\":\"20190101074110\",\"EFF_DATE_FLAG\":\"0\",\"EFF_RULE_ID\":\"1001\",\"EXP_DATE\":\"20991231235959\",\"EXP_DATE_FLAG\":\"0\",\"EXP_RULE_ID\":\"1023\",\"FEE_UNIT\":\"元/月\",\"FLOW_UNIT\":\"M\",\"IS_ATTR\":1,\"MODIFY_FLAG\":\"00\",\"MULTI_REL\":\"N\",\"OLD_EXP_DATE\":\"20190101074109\",\"OPERATION_FLAG\":\"C\",\"OTHER_DESC\":\"\",\"OUTSIDE_DESC\":\"每人月费1元，每人本地拨打家庭成员基本通话50分钟免费（包含V网分钟数），本地接听成员电话免费，当月申请当月生效。\",\"PRCATTR_LIMT_LIST\":{},\"PRC_CLASS\":\"0\",\"PRODPRC_FEE\":{},\"PROD_ID\":\"J0052\",\"PROD_PRCID\":\"PR060231\",\"PROD_PRC_DESC\":\"每人月费1元，每人本地拨打家庭成员基本通话50分钟免费（包含V网分钟数），本地接听成员电话免费，当月申请当月生效。\",\"PROD_PRC_FEE\":1.0,\"PROD_PRC_NAME\":\"家庭计划1元套餐（包50分钟）\",\"PROD_PRC_TYPE\":\"1\",\"REDOFLAG\":\"F\",\"SEL_FLAG\":\"0\",\"SEND_DESC\":\"\",\"SEND_FLOW\":0.0,\"SEND_SMS\":0,\"SMS_UNIT\":\"条\",\"SNED_CALL\":0.0,\"STATE\":\"A\",\"TEAM_MBR\":{\"TEAM_TYPE\":\"0\"},\"USER_RANGE\":\"1110000000100\"},{\"ATTR_FLAG\":\"Y\",\"ATTR_IDLIST\":\"SVC_SEP|IsShare\",\"BILLING_MODE\":\"A\",\"BRAND_ID\":\"0\",\"CALL_UNIT\":\"分钟\",\"CANCEL_DATE\":\"20190131235959\",\"CANCEL_RULE_ID\":\"1002\",\"CHINESE_INDEX\":\"JTJH2YTC(B100FZ)\",\"COM_FLAG\":\"0\",\"EFF_DATE\":\"20190101074110\",\"EFF_DATE_FLAG\":\"0\",\"EFF_RULE_ID\":\"1001\",\"EXP_DATE\":\"20991231235959\",\"EXP_DATE_FLAG\":\"0\",\"EXP_RULE_ID\":\"1023\",\"FEE_UNIT\":\"元/月\",\"FLOW_UNIT\":\"M\",\"IS_ATTR\":1,\"MODIFY_FLAG\":\"00\",\"MULTI_REL\":\"N\",\"OLD_EXP_DATE\":\"20190101074109\",\"OPERATION_FLAG\":\"C\",\"OTHER_DESC\":\"\",\"OUTSIDE_DESC\":\"每人月费2元，每人本地拨打家庭成员基本通话100分钟免费（包含V网分钟数），本地接听成员电话免费，当月申请当月生效。\",\"PRCATTR_LIMT_LIST\":{},\"PRC_CLASS\":\"0\",\"PRODPRC_FEE\":{},\"PROD_ID\":\"J0052\",\"PROD_PRCID\":\"PR060239\",\"PROD_PRC_DESC\":\"每人月费2元，每人本地拨打家庭成员基本通话100分钟免费（包含V网分钟数），本地接听成员电话免费，当月申请当月生效。\",\"PROD_PRC_FEE\":2.0,\"PROD_PRC_NAME\":\"家庭计划2元套餐(包100分钟)\",\"PROD_PRC_TYPE\":\"1\",\"REDOFLAG\":\"F\",\"SEL_FLAG\":\"0\",\"SEND_DESC\":\"\",\"SEND_FLOW\":0.0,\"SEND_SMS\":0,\"SMS_UNIT\":\"条\",\"SNED_CALL\":0.0,\"STATE\":\"A\",\"TEAM_MBR\":{\"TEAM_TYPE\":\"0\"},\"USER_RANGE\":\"1110000000100\"},{\"ATTR_FLAG\":\"Y\",\"ATTR_IDLIST\":\"DELAY|SVC_SEP|IsShare\",\"BILLING_MODE\":\"A\",\"BRAND_ID\":\"0\",\"CALL_UNIT\":\"分钟\",\"CANCEL_DATE\":\"20190131235959\",\"CANCEL_RULE_ID\":\"1002\",\"CHINESE_INDEX\":\"JTW2018B(B300FZ)\",\"COM_FLAG\":\"0\",\"EFF_DATE\":\"20190101074110\",\"EFF_DATE_FLAG\":\"0\",\"EFF_RULE_ID\":\"1001\",\"EXP_DATE\":\"20991231235959\",\"EXP_DATE_FLAG\":\"0\",\"EXP_RULE_ID\":\"1023\",\"FEE_UNIT\":\"元/月\",\"FLOW_UNIT\":\"M\",\"IS_ATTR\":1,\"MODIFY_FLAG\":\"00\",\"MULTI_REL\":\"N\",\"OLD_EXP_DATE\":\"20190101074109\",\"OPERATION_FLAG\":\"C\",\"OTHER_DESC\":\"\",\"OUTSIDE_DESC\":\"每人月费1元，群内成员每人每月可免费省内主叫300分钟，当月新办当月生效。群内最多19个省内成员。超出分钟数按照标准资费收取。\",\"PRCATTR_LIMT_LIST\":{},\"PRC_CLASS\":\"0\",\"PRODPRC_FEE\":{},\"PROD_ID\":\"J0052\",\"PROD_PRCID\":\"PR060252\",\"PROD_PRC_DESC\":\"每人月费1元，群内成员每人每月可免费省内主叫300分钟，当月新办当月生效。群内最多19个省内成员。超出分钟数按照标准资费收取。\",\"PROD_PRC_FEE\":1.0,\"PROD_PRC_NAME\":\"家庭网2018版（包300分钟）\",\"PROD_PRC_TYPE\":\"1\",\"REDOFLAG\":\"F\",\"SEL_FLAG\":\"0\",\"SEND_DESC\":\"\",\"SEND_FLOW\":0.0,\"SEND_SMS\":0,\"SMS_UNIT\":\"条\",\"SNED_CALL\":0.0,\"STATE\":\"A\",\"TEAM_MBR\":{\"TEAM_TYPE\":\"0\"},\"USER_RANGE\":\"1110000000100\"},{\"ATTR_FLAG\":\"Y\",\"ATTR_IDLIST\":\"SVC_SEP|IsShare\",\"BILLING_MODE\":\"A\",\"BRAND_ID\":\"0\",\"CALL_UNIT\":\"分钟\",\"CANCEL_DATE\":\"20190131235959\",\"CANCEL_RULE_ID\":\"1002\",\"CHINESE_INDEX\":\"JTJH2YTC(B200FZ)\",\"COM_FLAG\":\"0\",\"EFF_DATE\":\"20190101074110\",\"EFF_DATE_FLAG\":\"0\",\"EFF_RULE_ID\":\"1001\",\"EXP_DATE\":\"20991231235959\",\"EXP_DATE_FLAG\":\"0\",\"EXP_RULE_ID\":\"1023\",\"FEE_UNIT\":\"元/月\",\"FLOW_UNIT\":\"M\",\"IS_ATTR\":1,\"MODIFY_FLAG\":\"00\",\"MULTI_REL\":\"N\",\"OLD_EXP_DATE\":\"20190101074109\",\"OPERATION_FLAG\":\"C\",\"OTHER_DESC\":\"\",\"OUTSIDE_DESC\":\"家庭计划2元套餐每人月费2元，每人本地拨打家庭成员基本通话200分钟免费（包含V网分钟数），本地接听成员电话免费，当月申请当月生效。\",\"PRCATTR_LIMT_LIST\":{},\"PRC_CLASS\":\"0\",\"PRODPRC_FEE\":{},\"PROD_ID\":\"J0052\",\"PROD_PRCID\":\"PR060240\",\"PROD_PRC_DESC\":\"家庭计划2元套餐每人月费2元，每人本地拨打家庭成员基本通话200分钟免费（包含V网分钟数），本地接听成员电话免费，当月申请当月生效。\",\"PROD_PRC_FEE\":2.0,\"PROD_PRC_NAME\":\"家庭计划2元套餐(包200分钟)\",\"PROD_PRC_TYPE\":\"1\",\"REDOFLAG\":\"F\",\"SEL_FLAG\":\"0\",\"SEND_DESC\":\"\",\"SEND_FLOW\":0.0,\"SEND_SMS\":0,\"SMS_UNIT\":\"条\",\"SNED_CALL\":0.0,\"STATE\":\"A\",\"TEAM_MBR\":{\"TEAM_TYPE\":\"0\"},\"USER_RANGE\":\"1110000000100\"}],\"PROD_ID\":\"J0052\"},\"UP_AMOUNT\":19},\"PROMPT_MSG\":\"\",\"RETURN_CODE\":\"0\",\"RETURN_MSG\":\"OK\",\"RUN_IP\":\"152.56.136.141\",\"USER_MSG\":\"OK\"}}";
             
            //String json = "{\"ROOT\" : {\"RETURN_MSG\" : \"OK\",\"PROMPT_MSG\" : \"\",\"OUT_DATA\" : {\"PRC_LIST\" : {\"PROD_ID\" : \"J0052\",\"DATA\" : {\"STATE\" : \"A\",\"CANCEL_RULE_ID\" : \"1002\",\"OPERATION_FLAG\" : \"C\",\"MULTI_REL\" : \"N\",\"SEND_DESC\" : \"\",\"OTHER_DESC\" : \"\",\"BILLING_MODE\" : \"A\",\"FLOW_UNIT\" : \"M\",\"COM_FLAG\" : \"0\",\"SNED_CALL\" : 0.0,\"EXP_DATE\" : \"20991231235959\",\"SEL_FLAG\" : \"0\",\"CANCEL_DATE\" : \"20181231235959\",\"PROD_ID\" : \"J0052\",\"EFF_DATE_FLAG\" : \"0\",\"ATTR_IDLIST\" : \"DELAY|SVC_SEP|IsShare\",\"PRODPRC_FEE\" : {},\"OLD_EXP_DATE\" : \"20181225175653\",\"PROD_PRCID\" : \"PR060231\",\"PROD_PRC_TYPE\" : \"1\",\"TEAM_MBR\" : {\"TEAM_TYPE\" : \"0\"},\"CHINESE_INDEX\" : \"JTW2018B(B300FZ)\",\"FEE_UNIT\" : \"元/月\",\"IS_ATTR\" : 1,\"OUTSIDE_DESC\" : \"每人月费1元，群内成员每人每月可免费省内主叫300分钟，当月新办当月生效。群内最多19个省内成员。超出分钟数按照标准资费收取。\",\"EFF_RULE_ID\" : \"1001\",\"SMS_UNIT\" : \"条\",\"BRAND_ID\" : \"0\",\"USER_RANGE\" : \"1110000000100\",\"SEND_FLOW\" : 0.0,\"EFF_DATE\" : \"20181225175654\",\"PROD_PRC_FEE\" : 1.0,\"PROD_PRC_DESC\" : \"每人月费1元，群内成员每人每月可免费省内主叫300分钟，当月新办当月生效。群内最多19个省内成员。超出分钟数按照标准资费收取。\",\"EXP_DATE_FLAG\" : \"0\",\"ATTR_FLAG\" : \"Y\",\"PRCATTR_LIMT_LIST\" : {},\"PROD_PRC_NAME\" : \"家庭计划1元套餐（包50分钟）\",\"REDOFLAG\" : \"F\",\"SEND_SMS\" : 0,\"EXP_RULE_ID\" : \"1023\",\"MODIFY_FLAG\" : \"00\",\"PRC_CLASS\" : \"0\",\"CALL_UNIT\" : \"分钟\"}},\"DOWN_AMOUNT\" : 1,\"UP_AMOUNT\" : 19},\"RETURN_CODE\" : \"0\",\"USER_MSG\" : \"OK\",\"RUN_IP\" : \"152.56.136.141\",\"DETAIL_MSG\" : \"OK\"}}";
            jsonObject = JsonHelp.json2Obj(json);
        }catch(Exception e){
            logger.error("查询群资费支持的成员资费失败",e);
             return ret.setFail("查询群资费支持的成员资费失败");
        }
      
        if(NonUtil.isNon(jsonObject)){
             return ret.setFail("查询群资费支持的成员资费失败");
        }
        
        if( ! BossUtil.successCode(JsonUtil.getString(jsonObject, "ROOT.RETURN_CODE"))){   
            String retmsg=JsonUtil.getString(jsonObject, "ROOT.RETURN_MSG");
            return ret.setFail(NonUtil.isNon(retmsg)?"查询群资费支持的成员资费失败":retmsg);
        }
        return ret.setSuccess("OK", JsonUtil.getObject(jsonObject, "ROOT.OUT_DATA")  );
    }
    
	/**
	 * sRelChk
	 * 资费校验
	 */
    public static ReturnValueDomain<JSONObject> sRelChk(Map map , SellstdUserext u){
        ReturnValueDomain<JSONObject> ret=new ReturnValueDomain<JSONObject>();
        Map<String,Object> param=new HashMap<String,Object>();
        Map<String,Object> root=new HashMap<String,Object>();
        Object common_info=CommonTool.getCommon_info("1486",u.getBoss_no() );
        Object opr_info=CommonTool.getOpr_info(u.getChanneltype(), u.getGroup_id(), u.getBoss_no(), "1486", u.getRegin_id());
        Map<String,Object> request_info=new HashMap<String,Object>();
        Map<String,Object> busi_info_list=new HashMap<String,Object>();
        Map<String,Object> busi_info=new HashMap<String,Object>();
        Map<String,Object> sel_busi=new HashMap<String,Object>();
        Map<String,Object> sel_busi_list=new HashMap<String,Object>();
        
        busi_info.put("POWER_CODE", "15");
        busi_info.put("PROD_ID", map.get("prod_id"));
        busi_info.put("PROD_PRCID", map.get("prod_prcid"));
        
        if("1".equals(map.get("op_type_num"))){           //开户和添加成员
        	busi_info.put("ACTION", "N");
            busi_info.put("BAND_ID", "");
            busi_info.put("BILL_DAY", "1");
            busi_info.put("CHOOSE_FLAG", "N");
            busi_info.put("EFF_DATE", "");
            busi_info.put("EXP_DATE", "");        
            busi_info.put("FUN_EXP_DATE", "");        
            busi_info.put("GROUP_ID", "");      
            busi_info.put("ID_TYPE", "3");      
            busi_info.put("MODIFY_MAIN_PRC", "N");     
            busi_info.put("OP_CODE", "1004");     
            busi_info.put("OP_TYPE", "1");   
            busi_info.put("PARPROD_PRCID", "0");
            busi_info.put("PAR_GROUP_TYPE", "0");
            busi_info.put("PHONE_NO", map.get("phoneno"));
            busi_info.put("REORDER_FLAG", "");
            busi_info.put("TOTAL_FLAG", "");
            busi_info.put("SEL_BUSI_LIST", "{}");
        }else if("2".equals(map.get("op_type_num"))){    //变更第一次校验
        	busi_info.put("ACTION", "Y");
            busi_info.put("CUST_ID", "");
            busi_info.put("EFF_DATE", map.get("eff_date"));
            busi_info.put("EXP_DATE", "");
            busi_info.put("GROUP_ID", map.get("group_id"));
            busi_info.put("GROUP_ID_NO", "");
            busi_info.put("ID_NO", map.get("id_no"));
            busi_info.put("OP_CODE", "1106");
            busi_info.put("OP_FLAG", "0");
            busi_info.put("OP_TYPE", "2");
            busi_info.put("SEL_BUSI_LIST", "{}");
        }else if("3".equals(map.get("op_type_num"))){    //变更第二次校验
        	busi_info.put("ACTION", "N");
        	busi_info.put("CUST_ID", "");
        	busi_info.put("EFF_DATE", map.get("eff_date"));
            busi_info.put("EXP_DATE", "");
            busi_info.put("GROUP_ID", map.get("group_id"));
            busi_info.put("GROUP_ID_NO", map.get("group_id_no"));
            busi_info.put("ID_NO", map.get("id_no"));
            busi_info.put("OP_CODE", "1106");
            busi_info.put("OP_FLAG", "");
            busi_info.put("OP_TYPE", "1");
        	
            sel_busi.put("EFF_DATE", map.get("eff_date"));
            sel_busi.put("EXP_DATE", map.get("exp_date"));
            sel_busi.put("OP_TYPE", "2");
            sel_busi.put("PROD_ID", map.get("prod_id_2"));
            sel_busi.put("PROD_PRCID", map.get("prod_prcid_2"));
            
            sel_busi_list.put("SEL_BUSI", sel_busi);
            busi_info.put("SEL_BUSI_LIST", sel_busi_list);
        	
        }else if("4".equals(map.get("op_type_num"))){    //删除            离群           
        	busi_info.put("ACTION", "N");
            busi_info.put("CUST_ID", "");
            busi_info.put("EFF_DATE", map.get("eff_date"));
            busi_info.put("EXP_DATE", "");
            busi_info.put("GROUP_ID", map.get("group_id"));
            busi_info.put("GROUP_ID_NO", "");
            busi_info.put("ID_NO", map.get("id_no"));
            busi_info.put("OP_CODE", "1106");
            busi_info.put("OP_FLAG", "3");
            busi_info.put("OP_TYPE", "2");
            busi_info.put("SEL_BUSI_LIST", "{}");
        }
        
        busi_info_list.put("BUSI_INFO", busi_info);
        request_info.put("OPR_INFO", opr_info);
        request_info.put("BUSI_INFO_LIST", busi_info_list);
        
        root.put("COMMON_INFO", common_info);
        root.put("REQUEST_INFO", request_info);
        param.put("ROOT", root);
        String paramstr= JSONObject.toJSONString(param);
        ReqUrlConfig ReqUrlConfig=BeanUtil.getBean("reqUrlConfig", ReqUrlConfig.class);
        String http_url=ReqUrlConfig.getEsbUrl();
        String method = "sRelChk";
        JSONObject jsonObject=null;
        try{
             ReturnValueDomain<String> esb_ret =  CommonEsbUtil.sendPostRestful(http_url+method,paramstr);
             if(esb_ret.hasFail()||NonUtil.isNon(esb_ret.getObject())){
                 return ret.setFail("资费校验失败");
             }
             String json = esb_ret.getObject();
             //test
             //String json = "{\"ROOT\" : {\"DETAIL_MSG\" : \"OK\",\"OUT_DATA\" : {\"DATA\" : {\"EFF_DATE\" : \"20190101091901\",\"EXP_DATE\" : \"20991231235959\",\"LIMIT_FLAG\" : \"Y\",\"LIST\" : {},\"NOTES\" : \"[家庭包标准资费][PX000006][CPX006]可订购\",\"PROD_ID\" : \"CPX006\",\"PROD_PRCID\" : \"PX000006\",\"RELATION_TYPE\" : \"R\"}},\"PROMPT_MSG\" : \"\",\"RETURN_CODE\" : \"0\",\"RETURN_MSG\" : \"OK\",\"RUN_IP\" : \"152.56.136.141\",\"USER_MSG\" : \"OK\"}}";
             jsonObject = JsonHelp.json2Obj(json);
        }catch(Exception e){
            logger.error("资费校验失败",e);
             return ret.setFail("资费校验失败");
        }
      
        if(NonUtil.isNon(jsonObject)){
             return ret.setFail("资费校验失败");
        }
        
        if( ! BossUtil.successCode(JsonUtil.getString(jsonObject, "ROOT.RETURN_CODE"))){   
            String retmsg=JsonUtil.getString(jsonObject, "ROOT.RETURN_MSG");
            return ret.setFail(NonUtil.isNon(retmsg)?"资费校验失败":retmsg);
        }
        return ret.setSuccess("OK", JsonUtil.getObject(jsonObject, "ROOT.OUT_DATA.DATA")  );
    }
    
	/**
	 * 用户身份校验接口
	 */
	public static ReturnValueDomain<JSONObject> sQFuseMbr(Map map , SellstdUserext u){
		ReturnValueDomain<JSONObject> ret=new ReturnValueDomain<JSONObject>();
		Map<String,Object> param=new HashMap<String,Object>();
		Map<String,Object> root=new HashMap<String,Object>();
        Object common_info=CommonTool.getCommon_info("1486",u.getBoss_no() );
        Object opr_info=CommonTool.getOpr_info(u.getChanneltype(), u.getGroup_id(), u.getBoss_no(), "1486", u.getRegin_id());
        root.put("PHONE_NO", map.get("phoneno"));
        root.put("MASTER_SERV_ID", "1001");    //1001：G网；1102：宽带；1009：融合；2222：一网通    
        root.put("FLAG", "2");                                                       //不传此参数或FLAG=1，查询当前正在生效的成员,FLAG值不等于1，查询未失效的成员（包括预约生效的成员） 先传2
        root.put("GRP_TYPE_FLAG", "3");                                        //可为空，默认值0查询FD、F3、F1、FB；当GRP_TYPE_FLAG=1时查询FD、F3、F1、FB、XYWT；当GRP_TYPE_FLAG=2时查询FD、F3、F1、FB、XYWT，C0，D0，D1；本次传值0
		root.put("COMMON_INFO", common_info);
		root.put("OPR_INFO", opr_info);
		param.put("ROOT", root);
		String paramstr= JSONObject.toJSONString(param);
		ReqUrlConfig ReqUrlConfig=BeanUtil.getBean("reqUrlConfig", ReqUrlConfig.class);
		String http_url=ReqUrlConfig.getEsbUrl();
		String method = "sQFuseMbr";
		JSONObject jsonObject=null;
		try{
		     ReturnValueDomain<String> esb_ret =  CommonEsbUtil.sendPostRestful(http_url+method,paramstr);
		     if(esb_ret.hasFail()||NonUtil.isNon(esb_ret.getObject())){
		    	 return ret.setFail("查询用户身份校验失败");
		     }
		     String json = esb_ret.getObject();
		     //test 普通成员
		     //单G网
		     //String json = "{\"ROOT\": { \"DETAIL_MSG\": \"OK\", \"OUT_DATA\": {  \"GROUP_ID\": 0,  \"GRP_ID_NO\": 0,  \"ROLE_FLAG\": \"0\",  \"USER_ROLE_NAME\": \"普通用户\" }, \"PROMPT_MSG\": \"\", \"RETURN_CODE\": \"0\", \"RETURN_MSG\": \"OK\", \"RUN_IP\": \"152.56.136.141\", \"USER_MSG\": \"OK\"}}";
		     
		     //主角
		     //String json = "{\"ROOT\":{\"DETAIL_MSG\":\"OK\",\"OUT_DATA\":{\"GROUP_ID\":10011717453,\"GROUP_TYPE\":\"D1\",\"GRP_ID_NO\":11310015706363,\"GRP_PHONE\":\"11310015706363\",\"MBR_LIST\":{\"MBR_INFO\":[{\"EFF_DATE\":\"20140121142417\",\"EXP_DATE\":\"20991231235959\",\"MASTER_SERV_ID\":\"1001\",\"MBR_CONTRACT_NO\":11110000002543,\"MBR_CUST_ID\":11002607138958,\"MBR_ID_NO\":11110000002543,\"MBR_PHONE\":\"13856028061\",\"MBR_ROLE_FLAG\":\"\",\"MEMBER_ROLE_ID\":10002},{\"EFF_DATE\":\"20140121142509\",\"EXP_DATE\":\"20991231235959\",\"MASTER_SERV_ID\":\"1001\",\"MBR_CONTRACT_NO\":11410015168086,\"MBR_CUST_ID\":11002607138958,\"MBR_ID_NO\":11310015706313,\"MBR_PHONE\":\"18297975511\",\"MBR_ROLE_FLAG\":\"\",\"MEMBER_ROLE_ID\":10001},{\"EFF_DATE\":\"\",\"EXP_DATE\":\"\",\"MASTER_SERV_ID\":\"1007\",\"MBR_CONTRACT_NO\":11410015168138,\"MBR_CUST_ID\":11002607138958,\"MBR_ID_NO\":11310015706363,\"MBR_PHONE\":\"11310015706363\",\"MBR_ROLE_FLAG\":\"\",\"MEMBER_ROLE_ID\":0}]},\"MBR_NAME\":\"Y\",\"ROLE_FLAG\":\"1\",\"USER_ROLE_NAME\":\"家庭V网群主角\"},\"PROMPT_MSG\":\"\",\"RETURN_CODE\":\"0\",\"RETURN_MSG\":\"OK\",\"RUN_IP\":\"152.56.136.141\",\"USER_MSG\":\"OK\"}}";
		     
		     //普通成员
		     //String json = "{\"ROOT\":{\"DETAIL_MSG\":\"OK\",\"OUT_DATA\":{\"GROUP_ID\":10011717453,\"GROUP_TYPE\":\"D1\",\"GRP_ID_NO\":11310015706363,\"GRP_PHONE\":\"11310015706363\",\"MBR_LIST\":{\"MBR_INFO\":[{\"EFF_DATE\":\"20140121142509\",\"EXP_DATE\":\"20991231235959\",\"MASTER_SERV_ID\":\"\",\"MBR_CONTRACT_NO\":0,\"MBR_CUST_ID\":0,\"MBR_ID_NO\":11310015706313,\"MBR_PHONE\":\"18297975511\",\"MBR_ROLE_FLAG\":\"\",\"MEMBER_ROLE_ID\":10001}]},\"MBR_NAME\":\"N\",\"ROLE_FLAG\":\"2\",\"USER_ROLE_NAME\":\"家庭V网群成员\"},\"PROMPT_MSG\":\"\",\"RETURN_CODE\":\"0\",\"RETURN_MSG\":\"OK\",\"RUN_IP\":\"152.56.136.141\",\"USER_MSG\":\"OK\"}}";
		     
		     jsonObject = JsonHelp.json2Obj(json);
		}catch(Exception e){
			logger.error("查询用户身份校验失败",e);
			 return ret.setFail("查询用户身份校验失败");
		}
	  
		if(NonUtil.isNon(jsonObject)){
			 return ret.setFail("查询用户身份校验失败");
		}
		
		if( ! BossUtil.successCode(JsonUtil.getString(jsonObject, "ROOT.RETURN_CODE"))){   
			String retmsg=JsonUtil.getString(jsonObject, "ROOT.RETURN_MSG");
			return ret.setFail(NonUtil.isNon(retmsg)?"查询用户身份校验失败":retmsg);
		}
		
		return ret.setSuccess("OK",jsonObject);
		
	}
	
	/**
	 * 查询付费方式
	 * sDynSvc
	 */
	public static ReturnValueDomain<JSONObject> sDynSvc(Map map , SellstdUserext u){
		ReturnValueDomain<JSONObject> ret=new ReturnValueDomain<JSONObject>();
		Map<String,Object> param=new HashMap<String,Object>();
		Map<String,Object> root=new HashMap<String,Object>();
		Map<String,Object> svc_name=new HashMap<String,Object>();
		Map<String,Object> SVC_NAME=new HashMap<String,Object>();
		
        Object common_info=CommonTool.getCommon_info("1486",u.getBoss_no() );
        Object opr_info=CommonTool.getOpr_info(u.getChanneltype(), u.getGroup_id(), u.getBoss_no(), "1486", u.getRegin_id());
       
        root.put("svc_name", "d110605");
        root.put("SVC_NAME", "d110605");
		root.put("COMMON_INFO", common_info);
		root.put("OPR_INFO", opr_info);
		param.put("ROOT", root);
		String paramstr= JSONObject.toJSONString(param);
		ReqUrlConfig ReqUrlConfig=BeanUtil.getBean("reqUrlConfig", ReqUrlConfig.class);
		String http_url=ReqUrlConfig.getEsbUrl();
		String method = "sDynSvc";
		JSONObject jsonObject=null;
		try{
		     ReturnValueDomain<String> esb_ret =  CommonEsbUtil.sendPostRestful(http_url+method,paramstr);
		     if(esb_ret.hasFail()||NonUtil.isNon(esb_ret.getObject())){
		    	 return ret.setFail("调sDynSvc服务失败败");
		     }
		     String json = esb_ret.getObject();
		     //test 
		     //String json = "{ \"ROOT\": { \"RETURN_CODE\": 0, \"RETURN_MSG\": \"ok!\", \"DETAIL_MSG\": \"OK!\", \"PROMPT_MSG\": \"\", \"OUT_DATA\": { \"ROW\": [ { \"ACCT_ITEMS\": \"00000\", \"ACCT_ITEMS_NAME\": \"统付所有\" }, { \"ACCT_ITEMS\": \"39050\", \"ACCT_ITEMS_NAME\": \"只付月租\" }, { \"ACCT_ITEMS\": \"N\", \"ACCT_ITEMS_NAME\": \"不统付任何费用\" } ] }, \"USER_MSG\": \"处理成功!\" } }";
		     jsonObject = JsonHelp.json2Obj(json);
		}catch(Exception e){
			logger.error("调sDynSvc服务失败",e);
			 return ret.setFail("调sDynSvc服务失败");
		}
	  
		if(NonUtil.isNon(jsonObject)){
			 return ret.setFail("调sDynSvc服务失败");
		}
		
		if( ! BossUtil.successCode(JsonUtil.getString(jsonObject, "ROOT.RETURN_CODE"))){   
			String retmsg=JsonUtil.getString(jsonObject, "ROOT.RETURN_MSG");
			return ret.setFail(NonUtil.isNon(retmsg)?"调sDynSvc服务失败":retmsg);
		}
		
		return ret.setSuccess("OK",jsonObject);
		
	}
	
	/*
	 * 根据手机号获取id_no
	 * sBscUsrInfoQry
	 */
	public static ReturnValueDomain<JSONObject> sBscUsrInfoQry(Map map , SellstdUserext u){
		ReturnValueDomain<JSONObject> ret=new ReturnValueDomain<JSONObject>();
		Map<String,Object> param=new HashMap<String,Object>();
		Map<String,Object> root=new HashMap<String,Object>();
		Map<String,Object> request_info=new HashMap<String,Object>();
		Map<String,Object> busi_info_list=new HashMap<String,Object>();
		Map<String,Object> busi_info=new HashMap<String,Object>();
		
		
		busi_info.put("PHONE_NO", map.get("phoneno"));
		busi_info_list.put("BUSI_INFO", busi_info);
		
        Object common_info=CommonTool.getCommon_info("1486",u.getBoss_no() );
        Object opr_info=CommonTool.getOpr_info(u.getChanneltype(), u.getGroup_id(), u.getBoss_no(), "1486", u.getRegin_id());
        
        Map<String,Object> opr_info1=new HashMap<String,Object>();
        opr_info1.put("AUTHEN_CODE", "");
        opr_info1.put("AUTHEN_NAME", "");
        opr_info1.put("CHANNEL_TYPE", map.get("phoneno"));
        opr_info1.put("CONTACT_ID", "1120180150000001471605836");
        opr_info1.put("GROUP_ID", "111000000");
        opr_info1.put("IP_ADDRESS", MemberUtil.getIPAddress());
        opr_info1.put("LOGIN_NO", u.getBoss_no());
        opr_info1.put("LOGIN_PWD", "fb0bdec823cf4cdb");
        opr_info1.put("OP_CODE", "1486");
        opr_info1.put("REGION_ID", "11");
        opr_info1.put("REL_OP_CODE", "1486");
        
        request_info.put("BUSI_INFO_LIST", busi_info_list);
        request_info.put("OPR_INFO", opr_info1);
        
        root.put("COMMON_INFO", common_info);
		root.put("OPR_INFO", opr_info);
		root.put("REQUEST_INFO", request_info);

		param.put("ROOT", root);
		String paramstr= JSONObject.toJSONString(param);
		ReqUrlConfig ReqUrlConfig=BeanUtil.getBean("reqUrlConfig", ReqUrlConfig.class);
		String http_url=ReqUrlConfig.getEsbUrl();
		String method = "sBscUsrInfoQry";
		JSONObject jsonObject=null;
		try{
		     ReturnValueDomain<String> esb_ret =  CommonEsbUtil.sendPostRestful(http_url+method,paramstr);
		     if(esb_ret.hasFail()||NonUtil.isNon(esb_ret.getObject())){
		    	 return ret.setFail("调sJTJHProdPrcQry服务失败");
		     }
		     String json = esb_ret.getObject();
		     //test
		     //String json = "{\"ROOT\": {    \"DETAIL_MSG\": \"OK!\",    \"OUT_DATA\": {        \"ACCESS_TYPE\": \"002\",        \"ASSURE_FLAG\": \"0\",        \"BAK_FIELD\": \"null\",        \"BAK_FIELD1\": \"\",        \"BILLING_CYCLE_TYPE_ID\": 0,        \"BILL_END_TIME\": \"20991231000000\",        \"BILL_MODE_NAME\": \"后付费\",        \"BILL_START_TIME\": \"\",        \"BILL_TYPE\": 12,        \"BLONG_AREA\": \"合肥\",        \"BRAND_ID\": \"003\",        \"BRAND_NAME\": \"神州行\",        \"CARD_TYPE\": 1,        \"CMD_RIGHT\": 0,        \"COMPLETED_DATE\": \"19991001000000\",        \"CONTRACT_NO\": 11112600024547,        \"CREATE_DATE\": \"19991001000000\",        \"CREATE_FLAG\": \"Y\",        \"CREATE_LOGIN\": \"AZZZZ0ZZZ\",        \"CREDIT_CODE\": \"01\",        \"CUR_FEE\": 6860.000000,        \"CUR_SCORE\": 4055,        \"CUST_ID\": 11012600024547,        \"CUST_NAME\": \"＊＊军\",        \"EFF_DATE\": \"\",        \"FINISH_FLAG\": \"Y\",        \"FIRST_USEDATE\": \"19991001000000\",        \"FLAG_NAME\": \"正常\",        \"GRADE_FLAG\": \"是\",        \"GROUP_FLAG\": \"N\",        \"GROUP_ID\": \"111000002\",        \"GROUP_LIMIT_OWE\": 0.000000,        \"GROUP_NAME\": \"合肥\",        \"ID_NO\": 11112600024547,        \"INTERNET_ACCESS\": \"\",        \"IN_GROUP_NAME\": \"\",        \"IS_4G\": \"否\",        \"LIMIT_OWE\": 36800.000000,        \"LOGIN_ACCEPT\": 10038803941476,        \"LOGIN_NO\": \"Y18600WEB\",        \"MAIN_STATUS_CASH\": \"20\",        \"MASTER_SERV_ID\": \"1001\",        \"MASTER_SERV_NAME\": \"G网\",        \"NET_NO\": \"\",        \"OLD_RUN\": \"C\",        \"OPEN_NAME\": \"长丰县\",        \"OPEN_TIME\": \"19991001000000\",        \"OP_CODE\": \"1818\",        \"OP_TIME\": \"20170919090000\",        \"OWED_FLAG\": \"Y\",        \"OWNED_CHNL_ID\": \"111000010\",        \"OWNER_TYPE\": 1,        \"PASSWD_TYPE\": \"0\",        \"PHONE_NO\": \"13805603415\",        \"PHOTO_FLAG\": \"N\",        \"PRODPRCINS_ID\": 110064026247,        \"PROD_ID\": \"CP02W1\",        \"PROD_NAME\": \"新神州行（包含彩铃）\",        \"PROD_PRCID\": \"W1218928\",        \"PROD_PRC_NAME\": \"神州行大众卡09版Z8-NEW\",        \"REAL_USE_NAME\": \"长丰县\",        \"REGISTER_LEVEL\": \"E\",        \"REL_CUST_NAME\": \"冯明军\",        \"REL_ID_ICCID\": \"340121195508200010\",        \"RENT_DATE\": \"\",        \"RUN_CODE\": \"A\",        \"RUN_NAME\": \"正常\",        \"RUN_TIME\": \"20140422172818\",        \"SERVICE_GROUP\": \"111000002\",        \"SIM_NO\": \"898600501295F0504995\",        \"STAR_LEVEL\": \"3\",        \"STOP_FLAG\": \"Y\",        \"STOP_RENT_DATE\": \"20991231000000\",        \"USER_APPR_INFO\": \"已审核\",        \"USER_GRADE_CODE\": \"03\",        \"USER_LEVEL\": \"普通客户\",        \"USER_PASSWD\": \"BGCEQz\",        \"USER_ROLE_NAME\": \"普通用户\",        \"VIP_CARD_NO\": \"\",        \"VIP_CREATE_TYPE\": 0    },    \"PROMPT_MSG\": \"\",    \"RETURN_CODE\": 0,    \"RETURN_MSG\": \"ok!\",    \"USER_MSG\": \"处理成功!\"}}";
		     jsonObject = JsonHelp.json2Obj(json);
		}catch(Exception e){
			logger.error("调sBscUsrInfoQry服务失败",e);
			 return ret.setFail("调sBscUsrInfoQry服务失败");
		}
	  
		if(NonUtil.isNon(jsonObject)){
			 return ret.setFail("调sBscUsrInfoQry服务失败");
		}
		
		if( ! BossUtil.successCode(JsonUtil.getString(jsonObject, "ROOT.RETURN_CODE"))){   
			String retmsg=JsonUtil.getString(jsonObject, "ROOT.RETURN_MSG");
			return ret.setFail(NonUtil.isNon(retmsg)?"调sBscUsrInfoQry服务失败":retmsg);
		}
		
		return ret.setSuccess("OK",JsonUtil.getObject(jsonObject,"ROOT.OUT_DATA"));
		
	}
	
	/**
	 * 成员号码开户校验 sFamUsrChk  
	 * @param map
	 * @param u
	 * @return
	 */
	public static ReturnValueDomain<JSONObject> sFamUsrChk(Map map , SellstdUserext u){
		ReturnValueDomain<JSONObject> ret=new ReturnValueDomain<JSONObject>();
		Map<String,Object> param=new HashMap<String,Object>();
		Map<String,Object> root=new HashMap<String,Object>();
        Object common_info=CommonTool.getCommon_info("1486",u.getBoss_no() );
        Map<String, Object> opr_info=(Map<String, Object>)CommonTool.getOpr_info(u.getChanneltype(), u.getGroup_id(), u.getBoss_no(), "1486", u.getRegin_id());
        opr_info.put("AUTHEN_CODE", map.get("authen_code"));
        if ("3".equals(map.get("authen_code"))) {
            opr_info.put("AUTHEN_NAME", "验证码加上行回复");
        } else if ("4".equals(map.get("authen_code"))) {
            opr_info.put("AUTHEN_NAME", "服务密码");
        } else if ("23".equals(map.get("authen_code"))) {
            opr_info.put("AUTHEN_NAME", "二代身份证读取");
        } else {
            opr_info.put("AUTHEN_NAME", "");
        }
        root.put("COMMON_INFO", common_info);
        root.put("OPR_INFO", opr_info);
        
        root.put("LOGIN_NO", u.getLogin_no());  ////-工号
        root.put("GROUP_ID", map.get("group_id"));        //-开户  暂无群信息
        root.put("OP_CODE", "1486");
        root.put("OP_TYPE", map.get("op_type"));
        root.put("PHONE_NO", map.get("phoneno"));
        root.put("MEMBER_ROLE_ID", map.get("member_role_id"));  //////开户，家庭V网主角10002  成员10001
        root.put("PROD_PRCID", map.get("prod_prcid"));
        root.put("IS_CURRENT_EXPDATE", map.get("is_current_expdate"));//IS_CURRENT_EXPDATE 字段，月底失效的成员的时候返回，其值为Y
        if("A".equals(map.get("op_type"))){
            root.put("OBJECT_ID", map.get("object_id"));  
        }
        if("D".equals(map.get("op_type"))){
            root.put("MEMBER_ID", map.get("member_id")); 
        }
        
		param.put("ROOT", root);
		String paramstr= JSONObject.toJSONString(param);
		ReqUrlConfig ReqUrlConfig=BeanUtil.getBean("reqUrlConfig", ReqUrlConfig.class);
		String http_url=ReqUrlConfig.getEsbUrl();
		String method = "sFamUsrChk";
		JSONObject jsonObject=null;
		try{
		     ReturnValueDomain<String> esb_ret =  CommonEsbUtil.sendPostRestful(http_url+method,paramstr);
		     
		     String json = esb_ret.getObject();
		   
		     //test
		     //String json = "{\"ROOT\":{\"RETURN_MSG\":\"15755379149存在在途单，不允许继续办理！\",\"PROMPT_MSG\":\"\",\"OUT_DATA\":{},\"RETURN_CODE\":\"10011020200063018\",\"USER_MSG\":\"15755379149存在在途单，不允许继续办理！\",\"RUN_IP\":\"152.56.136.141\",\"DETAIL_MSG\":\"errCode:10011020200063018,errMsg:15755379149存在在途单，不允许继续办理！\"}}";
		     jsonObject = JsonHelp.json2Obj(json);
		}catch(Exception e){
			logger.error("成员号码开户校验失败",e);
			 return ret.setFail("成员号码开户校验失败");
		}
	  
		if(NonUtil.isNon(jsonObject)){
			 return ret.setFail("成员号码开户校验失败");
		}
		
		if( ! BossUtil.successCode(JsonUtil.getString(jsonObject, "ROOT.RETURN_CODE"))){ 
		    String retmsg="";
		    if("10011020200065279".equals(JsonUtil.getString(jsonObject, "ROOT.RETURN_CODE"))){
		        retmsg = "您已经申请办理过全国家庭V网业务，请勿重复操作！";
		    }
		    if("10010040201065176".equals(JsonUtil.getString(jsonObject, "ROOT.RETURN_CODE"))){
                retmsg = "该群正式成员和待回复确认成员数量总数已达上限，不允许再次添加！";
            }
		    if("500298554".equals(JsonUtil.getString(jsonObject, "ROOT.RETURN_CODE"))){
                retmsg = "工号信息不存在";
            }
		    if("10011020200062892".equals(JsonUtil.getString(jsonObject, "ROOT.RETURN_CODE"))){
		        retmsg = "用户与工号不归属同一个地市,不允许办理";
		    }
		    if("10011020200061035".equals(JsonUtil.getString(jsonObject, "ROOT.RETURN_CODE"))){
		        retmsg = "客户是非签约证件不允许家庭开户";
		    }
		    if("10011020200061214".equals(JsonUtil.getString(jsonObject, "ROOT.RETURN_CODE"))){
		        retmsg = "省内携号用户不允许开家庭计划";
		    }
		    if("10011020200060911".equals(JsonUtil.getString(jsonObject, "ROOT.RETURN_CODE"))){
                retmsg = "该用户欠费,无法办理当前业务";
            }
		    if(NonUtil.isNon(retmsg)){
		        retmsg = JsonUtil.getString(jsonObject, "ROOT.RETURN_MSG");
		    }
			return ret.setFail(NonUtil.isNon(retmsg)?"成员号码开户校验失败":retmsg);
		}
		return ret.setSuccess("OK",jsonObject);
	}
	
	/**
	 * 付费方式校验  
	 * sArgsValueChk
	 * @param map
	 * @param u
	 * @return
	 */
	public static ReturnValueDomain<JSONObject> sArgsValueChk(Map map , SellstdUserext u){
		ReturnValueDomain<JSONObject> ret=new ReturnValueDomain<JSONObject>();
		Map<String,Object> param=new HashMap<String,Object>();
		Map<String,Object> root=new HashMap<String,Object>();
		
		Map<String,Object> attr_info=new HashMap<String,Object>();
		attr_info.put("ATTR_ID", "ACC_D1");
		attr_info.put("ATTR_VALUE", map.get("attr_value"));//付费方式  主角付月租 传39050，主角不付任何费用  传 N
		attr_info.put("GROUP_OBJECT", "D1");
		attr_info.put("ID_NO", map.get("id_no"));			//成员ID_NO
		attr_info.put("MAIN_ID_NO", map.get("main_id_no")); //主角ID_NO
		attr_info.put("PROD_ID", map.get("prod_id"));		//群产品id
		attr_info.put("PROD_PRCID", map.get("prod_prcid")); //群资费id
		attr_info.put("SVC_ID", "");
		
		Map<String,Object> commoninfo=new HashMap<String,Object>();
        Object common_info=CommonTool.getCommon_info("1486",u.getBoss_no() );
        Object opr_info=CommonTool.getOpr_info(u.getChanneltype(), u.getGroup_id(), u.getBoss_no(), "1486", u.getRegin_id());
        
        root.put("ATTR_INFO", attr_info);
        root.put("COMMON_INFO", common_info);
        root.put("GROUP_OBJECT", "D1");
        root.put("ID_NO", map.get("id_no"));
        root.put("MASTER_SERV_ID", "1001");
        root.put("OPR_INFO", opr_info);
        root.put("PHONE_NO", map.get("phoneno"));	//添加的号码
        
		param.put("ROOT", root);
		String paramstr= JSONObject.toJSONString(param);
		ReqUrlConfig ReqUrlConfig=BeanUtil.getBean("reqUrlConfig", ReqUrlConfig.class);
		String http_url=ReqUrlConfig.getEsbUrl();
		String method = "sArgsValueChk";
		JSONObject jsonObject=null;
		try{
		     ReturnValueDomain<String> esb_ret =  CommonEsbUtil.sendPostRestful(http_url+method,paramstr);
		     if(esb_ret.hasFail()||NonUtil.isNon(esb_ret.getObject())){
		    	 return ret.setFail("支付服务方式校验失败");
		     }
		     String json = esb_ret.getObject();
		     
		     //test
		     //String json = "{\"ROOT\" : {\"DETAIL_MSG\" : \"OK\",\"OUT_DATA\" : {\"CHK_RESULT\" : {\"ATTR_CHK\" : {\"ATTR_ID\" : \"ACC_D1\",\"DESC\" : \"\",\"FLAG\" : \"2\",\"ID_NO\" : \"\",\"MASTER_SERV_ID\" : \"\"}}},\"PROMPT_MSG\" : \"\",\"RETURN_CODE\" : \"0\",\"RETURN_MSG\" : \"OK\",}}";
		     jsonObject = JsonHelp.json2Obj(json);
		}catch(Exception e){
			logger.error("付费方式校验失败",e);
			 return ret.setFail("付费方式校验失败");
		}
	  
		if(NonUtil.isNon(jsonObject)){
			 return ret.setFail("付费方式校验失败");
		}
		
		if( ! BossUtil.successCode(JsonUtil.getString(jsonObject, "ROOT.RETURN_CODE"))){   
			String retmsg=JsonUtil.getString(jsonObject, "ROOT.RETURN_MSG");
			return ret.setFail(NonUtil.isNon(retmsg)?"付费方式校验失败":retmsg);
		}
		
		return ret.setSuccess("OK", JsonUtil.getObject(jsonObject, "ROOT.OUT_DATA.CHK_RESULT.ATTR_CHK"));
		
	}
	
	/**
	 * 调用短厅的开户接口 PushSerialSmsParseNew(调用的服务名首字母大写)
	 * @param map
	 * @param u
	 * @return
	 */
	public static ReturnValueDomain<JSONObject> pushSerialSmsParseNew(Map map , SellstdUserext u){
		ReturnValueDomain<JSONObject> ret=new ReturnValueDomain<JSONObject>();
		List push_list = new ArrayList();
		
		
		Map<String,Object> param=new HashMap<String,Object>();
		Map<String,Object> root=new HashMap<String,Object>();
		Map<String,Object> info=new HashMap<String,Object>();
		
		info.put("SMS_CONTENT", map.get("sms_content"));
		info.put("SRC_NO", "10086");   
		info.put("PHONE_NO", map.get("memberPhone"));
        //SERIAL_NO：主角号码#主角资费id#付费方式#0#短号#副角号码#副角资费id#付费方式#短号#0#群产品id#群资费id
		info.put("SERIAL_NO",map.get("serial_no"));
		info.put("SERV_NO", map.get("serv_no")); 
		
        push_list.add(info);
        root.put("PUSH_LIST", push_list);
        param.put("ROOT", root);
		String paramstr= JSONObject.toJSONString(param);
		ReqUrlConfig ReqUrlConfig=BeanUtil.getBean("reqUrlConfig", ReqUrlConfig.class);
		String http_url=ReqUrlConfig.getEsbUrl();
		String method = "PushSerialSmsParseNew";
		JSONObject jsonObject=null;
		try{
		     ReturnValueDomain<String> esb_ret =  CommonEsbUtil.sendPostRestful(http_url+method,paramstr);
		     if(esb_ret.hasFail()||NonUtil.isNon(esb_ret.getObject())){
		    	 return ret.setFail("查询用户身份校验失败");
		     }
		     String json = esb_ret.getObject();
		     //test
		     //String json = "{\"ROOT\":{\"RETURN_MSG\":\"成功\",\"RETURN_CODE\":\"0\"}}";
		     jsonObject = JsonHelp.json2Obj(json);
		}catch(Exception e){
			 logger.error("开户失败",e);
			 return ret.setFail("开户失败");
		}
	  
		if(NonUtil.isNon(jsonObject)){
			 return ret.setFail("开户失败");
		}
		
		if( ! BossUtil.successCode(JsonUtil.getString(jsonObject, "ROOT.RETURN_CODE"))){   
			String retmsg=JsonUtil.getString(jsonObject, "ROOT.RETURN_MSG");
			return ret.setFail(NonUtil.isNon(retmsg)?"开户失败":retmsg);
		}
		
		return ret.setSuccess("OK",jsonObject);
		
	}
	
	/*
     * sQUsrOfFamily
     */
    public static ReturnValueDomain<HomeVnetDomain> sQUsrOfFamily(SqAllCust in) {
        ReturnValueDomain<HomeVnetDomain> ret = new ReturnValueDomain<HomeVnetDomain>();
            StringBuffer XML = new StringBuffer();
            XML.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
            XML.append("<ROOT>");
            XML.append("    <OBJECT_ID type=\"string\">"+in.getId_no()+"</OBJECT_ID>");
            XML.append("    <QRY_FLAG type=\"string\">Y</QRY_FLAG>");
            XML.append("</ROOT>");
            XmlHelper xh=null;
            try {
               String method = "sQUsrOfFamily";
               ReqUrlConfig ReqUrlConfig = BeanUtil.getBean("reqUrlConfig",ReqUrlConfig.class);
               String http_url = ReqUrlConfig.getEsbUrl();
               ReturnValueDomain<String> xml_ret =  CommonEsbUtil.sendPostRestful(http_url+method,XML.toString());
               if (xml_ret.hasFail()) {
                   return ret.setFail("调sQUsrOfFamily服务失败");
               }
               String xmlstr = xml_ret.getObject();
               
               //String xmlstr = "<?xml version=\"1.0\" encoding=\"GBK\"?><ROOT><RETURN_MSG type=\"string\">OK</RETURN_MSG><PROMPT_MSG type=\"string\"/><OUT_DATA><PRODPRC_INFO><STATE type=\"string\">A</STATE><CANCEL_RULE_ID type=\"string\">1002</CANCEL_RULE_ID><MAX_NUM type=\"string\">99</MAX_NUM><TEAM_TYPE type=\"string\"/><BILLING_MODE type=\"string\">A</BILLING_MODE><MIN_NUM type=\"string\">0</MIN_NUM><GROUP_TYPE type=\"string\"/><COM_FLAG type=\"string\"/><PRICING_ID type=\"string\">0</PRICING_ID><EXP_DATE type=\"string\">20991231235959</EXP_DATE><VERSION type=\"string\">0</VERSION><PROD_ID type=\"string\">CPX006</PROD_ID> 群产品id<PROD_NAME type=\"string\">家庭包</PROD_NAME><PROD_PRCID type=\"string\">PX000006</PROD_PRCID>群资费id<PROD_PRC_TYPE type=\"string\">0</PROD_PRC_TYPE><CHINESE_INDEX type=\"string\">JTBBZZF</CHINESE_INDEX><EFF_RULE_ID type=\"string\">1001</EFF_RULE_ID><BRAND_ID type=\"string\">0  </BRAND_ID><USER_RANGE type=\"string\">10100000001</USER_RANGE><EFF_DATE type=\"string\">20090428215552</EFF_DATE><PROD_PRC_DESC type=\"string\">家庭包标准资费</PROD_PRC_DESC><PROD_TYPE type=\"string\"/><REDOFLAG type=\"string\">F</REDOFLAG><PROD_PRC_NAME type=\"string\">家庭包标准资费</PROD_PRC_NAME><EXP_RULE_ID type=\"string\">1023</EXP_RULE_ID><MODIFY_FLAG type=\"string\">00</MODIFY_FLAG><PRC_CLASS type=\"string\">0</PRC_CLASS></PRODPRC_INFO></OUT_DATA><RETURN_CODE type=\"string\">0</RETURN_CODE><USER_MSG type=\"string\">OK</USER_MSG><RUN_IP type=\"string\">10.243.8.62</RUN_IP><DETAIL_MSG type=\"string\">OK</DETAIL_MSG></ROOT>";
               xh=new XmlHelper(xmlstr);
               
            } catch (Exception e) {
               logger.error("调sQUsrOfFamily服务失败",e);
               return ret.setFail("调sQUsrOfFamily服务失败");
            }
            if (NonUtil.isNon(xh)) {
                return ret.setFail("调sQUsrOfFamily服务失败");
            }else{
                if("0".equals(xh.getNodeText("/ROOT/RETURN_CODE"))){//0 是校验通过
                    ret.setSuccess("调sQUsrOfFamily服务成功!");
                    HomeVnetDomain homeVnetDomain = new HomeVnetDomain();
                    homeVnetDomain.setMember_role_id(xh.getNodeText("/ROOT/OUT_DATA/MEMBER_ROLE_ID"));
                    ret.setObject(homeVnetDomain);
                }else{
                    ret.setFail("调sQUsrOfFamily服务失败");
                }
            }
            return ret;
    }
    /**
     * 查询V网群成员信息 sQUserGroupMbr
     * @param map
     * @param u
     * @return
     */
    public static ReturnValueDomain<JSONObject> sQUserGroupMbr(Map map , SellstdUserext u){
        ReturnValueDomain<JSONObject> ret=new ReturnValueDomain<JSONObject>();
        Map<String,Object> param=new HashMap<String,Object>();
        Map<String,Object> root=new HashMap<String,Object>();
        root.put("ROLE_FLAG", map.get("role_flag"));
        
        Map<String,Object> request_info=new HashMap<String,Object>();
        root.put("REQUEST_INFO",request_info);
        
        Map<String,Object> opr_info=new HashMap<String,Object>();
        request_info.put("OPR_INFO", opr_info);
        opr_info.put("LOGIN_NO", u.getBoss_no());
        Map<String,Object> busi_info_list=new HashMap<String,Object>();
        request_info.put("BUSI_INFO_LIST", busi_info_list);
        
        Map<String,Object> busi_info=new HashMap<String,Object>();
        busi_info_list.put("BUSI_INFO", busi_info);
        busi_info.put("GROUP_ID", map.get("group_id"));
        busi_info.put("ID_NO", map.get("id_no"));
        busi_info.put("ROLE_FLAG", map.get("role_flag"));
        busi_info.put("TEAM_ID", "0");
        
        
        param.put("ROOT", root);
        String paramstr= JSONObject.toJSONString(param);
        ReqUrlConfig ReqUrlConfig=BeanUtil.getBean("reqUrlConfig", ReqUrlConfig.class);
        String http_url=ReqUrlConfig.getEsbUrl();
        String method = "sQUserGroupMbr";
        JSONObject jsonObject=null;
        try{
             ReturnValueDomain<String> esb_ret =  CommonEsbUtil.sendPostRestful(http_url+method,paramstr);
             if(esb_ret.hasFail()||NonUtil.isNon(esb_ret.getObject())){
                 return ret.setFail("查询用户身份校验失败");
             }
             String json = esb_ret.getObject();
             //test

             //String json = "{\"ROOT\":{\"DETAIL_MSG\":\"OK\",\"OUT_DATA\":{\"MBR_INFO\":{\"GROUP_MBR\":{\"ALLOW_CNT\":\"17\",\"GROUP_CODE\":\"13856028061\",\"GROUP_DESC\":\"＊＊兵\",\"GROUP_ID\":\"10011717453\",\"GROUP_TYPE\":\"D1\",\"GRPID_NO\":\"11310015706363\",\"LOCK_FLAG\":\"0\",\"MAINMEMPROD_PRCID\":\"PR060604\",\"MARSTER_NUM\":\"13856028061\",\"MEMBER_INFO\":[{\"ADDPRODPRCINS_ID\":60000871451118,\"ADDPROD_ID\":\"J0052\",\"ADDPROD_NAME\":\"家庭计划\",\"ADDPROD_PRCID\":\"PR060231\",\"ADDPROD_PRC_NAME\":\"家庭计划1元套餐（包50分钟）\",\"BILL_TYPE\":\"只付月租\",\"BILL_VALUE\":\"39050\",\"BRAND_ID\":\"003\",\"BRAND_NAME\":\"神州行\",\"CANCEL_DATE\":\"20190131235959\",\"CARD_NAME\":\"普通客户\",\"CARD_TYPE\":\"1\",\"CONTRACT_NO\":\"11110000002543\",\"CUST_ID\":\"11002607138958\",\"CUST_NAME\":\"＊＊兵\",\"EFF_DATE\":\"20140121142417\",\"ELEMENT_ID\":\"1060\",\"EXP_DATE\":\"20991231235959\",\"GROUP_ID\":\"0\",\"IF_DHDX\":\"0\",\"LEAVE_FLAG\":\"N\",\"LOCK_FLAG\":\"0\",\"MASTER_SERV_ID\":\"1001\",\"MEMBER_DESC\":\"member_desc\",\"MEMBER_ID\":\"10039578467\",\"MEMBER_ROLE_DESC\":\"家庭计划主角角色\",\"MEMBER_ROLE_ID\":\"10002\",\"MEMBER_ROLE_NAME\":\"主角\",\"MEMBER_ROLE_TYPE\":\"D1\",\"MEM_PROD_LIST\":{\"MEM_PROD\":{\"ELEMENT_ID\":\"1060\",\"MASTER_SERV_ID\":\"1001\",\"MEMCANCEL_DATE\":\"20190131235959\",\"MEMPRC_EFF_DATE\":\"20140121142417\",\"MEMPRC_EXP_DATE\":\"20991231235959\",\"MEMPRODPRCINS_ID\":\"60000871451118\",\"MEMPROD_ID\":\"J0052\",\"MEMPROD_MAIN_FLAG\":\"1\",\"MEMPROD_NAME\":\"家庭计划\",\"MEMPROD_PRCID\":\"PR060604\",\"MEMPROD_PRC_NAME\":\"家庭计划1元套餐（包50分钟）\",\"NOW_EFF\":\"1\"}},\"MIN_AMOUNT\":\"1\",\"MsAX_AMOUNT\":\"1\",\"OBJECT_ID\":\"11110000002543\",\"OBJECT_TYPE\":\"1\",\"PHONE_NO\":\"13856028061\",\"PHONE_NO1\":\"13856028061\",\"PRC_EFF_DATE\":\"20140121142417\",\"PRC_EXP_DATE\":\"20991231235959\",\"PROD_ID\":\"CP02W1\",\"PROD_MAIN_FLAG\":\"1\",\"PROD_NAME\":\"新神州行（包含彩铃）\",\"PROD_PRCID\":\"W1018803\",\"PROD_PRC_NAME\":\"神州行顺心卡NEW\",\"REGION_ID\":\"11\",\"RUN_NAME\":\"正常\",\"SHORTNUM_FLAG\":\"\",\"SHORT_NUM\":\"520\",\"STATE\":\"A\",\"TEAM_ID\":\"0\"},{\"ADDPRODPRCINS_ID\":60000871453506,\"ADDPROD_ID\":\"J0052\",\"ADDPROD_NAME\":\"家庭计划\",\"ADDPROD_PRCID\":\"PR060231\",\"ADDPROD_PRC_NAME\":\"家庭计划1元套餐（包50分钟）\",\"BILL_TYPE\":\"只付月租\",\"BILL_VALUE\":\"39050\",\"BRAND_ID\":\"003\",\"BRAND_NAME\":\"神州行\",\"CANCEL_DATE\":\"20190131235959\",\"CARD_NAME\":\"普通客户\",\"CARD_TYPE\":\"1\",\"CONTRACT_NO\":\"11410015168086\",\"CUST_ID\":\"11002607138958\",\"CUST_NAME\":\"＊＊兵\",\"EFF_DATE\":\"20140121142509\",\"ELEMENT_ID\":\"1060\",\"EXP_DATE\":\"20991231235959\",\"GROUP_ID\":\"0\",\"IF_DHDX\":\"0\",\"LEAVE_FLAG\":\"N\",\"LOCK_FLAG\":\"0\",\"MASTER_SERV_ID\":\"1001\",\"MEMBER_DESC\":\"member_desc\",\"MEMBER_ID\":\"10039578468\",\"MEMBER_ROLE_DESC\":\"家庭计划普通角色\",\"MEMBER_ROLE_ID\":\"10001\",\"MEMBER_ROLE_NAME\":\"普通\",\"MEMBER_ROLE_TYPE\":\"D1\",\"MEM_PROD_LIST\":{\"MEM_PROD\":{\"ELEMENT_ID\":\"1060\",\"MASTER_SERV_ID\":\"1001\",\"MEMCANCEL_DATE\":\"20190131235959\",\"MEMPRC_EFF_DATE\":\"20140121142509\",\"MEMPRC_EXP_DATE\":\"20991231235959\",\"MEMPRODPRCINS_ID\":\"60000871453506\",\"MEMPROD_ID\":\"J0052\",\"MEMPROD_MAIN_FLAG\":\"1\",\"MEMPROD_NAME\":\"家庭计划\",\"MEMPROD_PRCID\":\"PR060604\",\"MEMPROD_PRC_NAME\":\"家庭计划1元套餐（包50分钟）\",\"NOW_EFF\":\"1\"}},\"MIN_AMOUNT\":\"0\",\"MsAX_AMOUNT\":\"999\",\"OBJECT_ID\":\"11310015706313\",\"OBJECT_TYPE\":\"1\",\"PHONE_NO\":\"18297975511\",\"PHONE_NO1\":\"18297975511\",\"PRC_EFF_DATE\":\"20140121142509\",\"PRC_EXP_DATE\":\"20991231235959\",\"PROD_ID\":\"CP16W2\",\"PROD_MAIN_FLAG\":\"1\",\"PROD_NAME\":\"乡情卡(来电显示)\",\"PROD_PRCID\":\"W1218802\",\"PROD_PRC_NAME\":\"乡情卡2013版13元\",\"REGION_ID\":\"11\",\"RUN_NAME\":\"正常\",\"SHORTNUM_FLAG\":\"\",\"SHORT_NUM\":\"521\",\"STATE\":\"A\",\"TEAM_ID\":\"0\"}],\"NOW_CNT\":\"2\",\"PAR_PROD_ID\":\"CPX026\",\"PAR_PROD_PRCID\":\"PX000026\",\"SUM_CNT\":\"19\",\"USEFLAG\":\"N\"}}},\"PROMPT_MSG\":\"\",\"RETURN_CODE\":\"0\",\"RETURN_MSG\":\"OK\",\"RUN_IP\":\"152.56.136.141\",\"USER_MSG\":\"OK\"}}";
            //String json = "{\"ROOT\" : {\"RETURN_MSG\" : \"OK\",\"PROMPT_MSG\" : \"\",\"OUT_DATA\" : {\"MBR_INFO\" : {\"GROUP_MBR\" : {\"MEMBER_INFO\" : [{\"ADDPROD_NAME\" : \"家庭计划\",\"PRC_EFF_DATE\" : \"20130127100038\",\"PHONE_NO1\" : \"13655574877\",\"IF_DHDX\" : \"0\",\"MsAX_AMOUNT\" : \"1\",\"OBJECT_ID\" : \"21060100028201\",\"BRAND_NAME\" : \"动感地带\",\"ADDPROD_ID\" : \"J0052\",\"PHONE_NO\" : \"13655574877\",\"CANCEL_DATE\" : \"20181231235959\",\"MASTER_SERV_ID\" : \"1001\",\"PROD_NAME\" : \"任我用套餐2\",\"LEAVE_FLAG\" : \"N\",\"BILL_TYPE\" : \"不统付任何费用\",\"GROUP_ID\" : \"0\",\"PRC_EXP_DATE\" : \"20991231235959\",\"RUN_NAME\" : \"正常\",\"EFF_DATE\" : \"20130127100038\",\"CUST_NAME\" : \"＊＊军\",\"PROD_MAIN_FLAG\" : \"1\",\"PROD_PRC_NAME\" : \"任我用198元（2018版）\",\"ADDPROD_PRC_NAME\" : \"家庭计划套餐1元（包100分钟）\",\"MEMBER_ROLE_TYPE\" : \"D1\",\"STATE\" : \"A\",\"OBJECT_TYPE\" : \"1\",\"MEMBER_ID\" : \"10031748061\",\"MEMBER_ROLE_NAME\" : \"主角\",\"ADDPROD_PRCID\" : \"PR060276\",\"EXP_DATE\" : \"20991231235959\",\"SHORT_NUM\" : \"520\",\"CUST_ID\" : \"21000000372283\",\"PROD_ID\" : \"CP18W8\",\"ADDPRODPRCINS_ID\" : 60000654282399,\"PROD_PRCID\" : \"CP18W808\",\"CARD_TYPE\" : \"5\",\"SHORTNUM_FLAG\" : \"\",\"MEMBER_ROLE_ID\" : \"10002\",\"BRAND_ID\" : \"002\",\"CARD_NAME\" : \"贵宾卡\",\"MIN_AMOUNT\" : \"1\",\"REGION_ID\" : \"21\",\"BILL_VALUE\" : \"N\",\"TEAM_ID\" : \"0\",\"MEMBER_DESC\" : \"member_desc\",\"LOCK_FLAG\" : \"0\",\"MEMBER_ROLE_DESC\" : \"家庭计划主角角色\",\"MEM_PROD_LIST\" : {\"MEM_PROD\" : {\"MEMPRODPRCINS_ID\" : \"60000654282399\",\"MEMPROD_NAME\" : \"家庭计划\",\"MEMPROD_PRC_NAME\" : \"家庭计划套餐1元（包100分钟）\",\"MEMPRC_EFF_DATE\" : \"20130127100038\",\"MEMPRC_EXP_DATE\" : \"20991231235959\",\"MASTER_SERV_ID\" : \"1001\",\"MEMCANCEL_DATE\" : \"20181231235959\",\"MEMPROD_MAIN_FLAG\" : \"1\",\"ELEMENT_ID\" : \"1057\",\"MEMPROD_ID\" : \"J0052\",\"MEMPROD_PRCID\" : \"PR060276\",\"NOW_EFF\" : \"1\"}},\"ELEMENT_ID\" : \"1057\",\"CONTRACT_NO\" : \"21060100028201\"}, {\"ADDPROD_NAME\" : \"家庭计划\",\"PRC_EFF_DATE\" : \"20130127100106\",\"PHONE_NO1\" : \"13855704877\",\"IF_DHDX\" : \"0\",\"MsAX_AMOUNT\" : \"999\",\"OBJECT_ID\" : \"21100000365013\",\"BRAND_NAME\" : \"动感地带\",\"ADDPROD_ID\" : \"J0052\",\"PHONE_NO\" : \"13855704877\",\"CANCEL_DATE\" : \"20181231235959\",\"MASTER_SERV_ID\" : \"1001\",\"PROD_NAME\" : \"任我用套餐2\",\"LEAVE_FLAG\" : \"N\",\"BILL_TYPE\" : \"不统付任何费用\",\"GROUP_ID\" : \"0\",\"PRC_EXP_DATE\" : \"20991231235959\",\"RUN_NAME\" : \"正常\",\"EFF_DATE\" : \"20130127100106\",\"CUST_NAME\" : \"＊＊军\",\"PROD_MAIN_FLAG\" : \"1\",\"PROD_PRC_NAME\" : \"任我用108元（2018版）\",\"ADDPROD_PRC_NAME\" : \"家庭计划套餐1元（包100分钟）\",\"MEMBER_ROLE_TYPE\" : \"D1\",\"STATE\" : \"A\",\"OBJECT_TYPE\" : \"1\",\"MEMBER_ID\" : \"10031748062\",\"MEMBER_ROLE_NAME\" : \"普通\",\"ADDPROD_PRCID\" : \"PR060276\",\"EXP_DATE\" : \"20991231235959\",\"SHORT_NUM\" : \"521\",\"CUST_ID\" : \"21000000372283\",\"PROD_ID\" : \"CP18W8\",\"ADDPRODPRCINS_ID\" : 60000654282584,\"PROD_PRCID\" : \"CP18W806\",\"CARD_TYPE\" : \"1\",\"SHORTNUM_FLAG\" : \"\",\"MEMBER_ROLE_ID\" : \"10001\",\"BRAND_ID\" : \"002\",\"CARD_NAME\" : \"普通客户\",\"MIN_AMOUNT\" : \"0\",\"REGION_ID\" : \"21\",\"BILL_VALUE\" : \"N\",\"TEAM_ID\" : \"0\",\"MEMBER_DESC\" : \"member_desc\",\"LOCK_FLAG\" : \"0\",\"MEMBER_ROLE_DESC\" : \"家庭计划普通角色\",\"MEM_PROD_LIST\" : {\"MEM_PROD\" : {\"MEMPRODPRCINS_ID\" : \"60000654282584\",\"MEMPROD_NAME\" : \"家庭计划\",\"MEMPROD_PRC_NAME\" : \"家庭计划套餐1元（包100分钟）\",\"MEMPRC_EFF_DATE\" : \"20130127100106\",\"MEMPRC_EXP_DATE\" : \"20991231235959\",\"MASTER_SERV_ID\" : \"1001\",\"MEMCANCEL_DATE\" : \"20181231235959\",\"MEMPROD_MAIN_FLAG\" : \"1\",\"ELEMENT_ID\" : \"1057\",\"MEMPROD_ID\" : \"J0052\",\"MEMPROD_PRCID\" : \"PR060276\",\"NOW_EFF\" : \"1\"}},\"ELEMENT_ID\" : \"1057\",\"CONTRACT_NO\" : \"21100000365013\"}, {\"ADDPROD_NAME\" : \"家庭计划\",\"PRC_EFF_DATE\" : \"20130127100141\",\"PHONE_NO1\" : \"15855730175\",\"IF_DHDX\" : \"0\",\"MsAX_AMOUNT\" : \"999\",\"OBJECT_ID\" : \"21090394132083\",\"BRAND_NAME\" : \"动感地带\",\"ADDPROD_ID\" : \"J0052\",\"PHONE_NO\" : \"15855730175\",\"CANCEL_DATE\" : \"20181231235959\",\"MASTER_SERV_ID\" : \"1001\",\"PROD_NAME\" : \"任我用套餐2\",\"LEAVE_FLAG\" : \"N\",\"BILL_TYPE\" : \"不统付任何费用\",\"GROUP_ID\" : \"0\",\"PRC_EXP_DATE\" : \"20991231235959\",\"RUN_NAME\" : \"正常\",\"EFF_DATE\" : \"20130127100141\",\"CUST_NAME\" : \"＊＊勇\",\"PROD_MAIN_FLAG\" : \"1\",\"PROD_PRC_NAME\" : \"任我用138元（2018版）\",\"ADDPROD_PRC_NAME\" : \"家庭计划套餐1元（包100分钟）\",\"MEMBER_ROLE_TYPE\" : \"D1\",\"STATE\" : \"A\",\"OBJECT_TYPE\" : \"1\",\"MEMBER_ID\" : \"10031748063\",\"MEMBER_ROLE_NAME\" : \"普通\",\"ADDPROD_PRCID\" : \"PR060276\",\"EXP_DATE\" : \"20991231235959\",\"SHORT_NUM\" : \"522\",\"CUST_ID\" : \"21090394132082\",\"PROD_ID\" : \"CP18W8\",\"ADDPRODPRCINS_ID\" : 60000654282729,\"PROD_PRCID\" : \"CP18W807\",\"CARD_TYPE\" : \"1\",\"SHORTNUM_FLAG\" : \"\",\"MEMBER_ROLE_ID\" : \"10001\",\"BRAND_ID\" : \"002\",\"CARD_NAME\" : \"普通客户\",\"MIN_AMOUNT\" : \"0\",\"REGION_ID\" : \"21\",\"BILL_VALUE\" : \"N\",\"TEAM_ID\" : \"0\",\"MEMBER_DESC\" : \"member_desc\",\"LOCK_FLAG\" : \"0\",\"MEMBER_ROLE_DESC\" : \"家庭计划普通角色\",\"MEM_PROD_LIST\" : {\"MEM_PROD\" : {\"MEMPRODPRCINS_ID\" : \"60000654282729\",\"MEMPROD_NAME\" : \"家庭计划\",\"MEMPROD_PRC_NAME\" : \"家庭计划套餐1元（包100分钟）\",\"MEMPRC_EFF_DATE\" : \"20130127100141\",\"MEMPRC_EXP_DATE\" : \"20991231235959\",\"MASTER_SERV_ID\" : \"1001\",\"MEMCANCEL_DATE\" : \"20181231235959\",\"MEMPROD_MAIN_FLAG\" : \"1\",\"ELEMENT_ID\" : \"1057\",\"MEMPROD_ID\" : \"J0052\",\"MEMPROD_PRCID\" : \"PR060276\",\"NOW_EFF\" : \"1\"}},\"ELEMENT_ID\" : \"1057\",\"CONTRACT_NO\" : \"21090394132083\"}, {\"ADDPROD_NAME\" : \"家庭计划\",\"PRC_EFF_DATE\" : \"20130127100211\",\"PHONE_NO1\" : \"13855700611\",\"IF_DHDX\" : \"0\",\"MsAX_AMOUNT\" : \"999\",\"OBJECT_ID\" : \"21100000406092\",\"BRAND_NAME\" : \"动感地带\",\"ADDPROD_ID\" : \"J0052\",\"PHONE_NO\" : \"13855700611\",\"CANCEL_DATE\" : \"20181231235959\",\"MASTER_SERV_ID\" : \"1001\",\"PROD_NAME\" : \"任我用套餐2\",\"LEAVE_FLAG\" : \"N\",\"BILL_TYPE\" : \"不统付任何费用\",\"GROUP_ID\" : \"0\",\"PRC_EXP_DATE\" : \"20991231235959\",\"RUN_NAME\" : \"正常\",\"EFF_DATE\" : \"20130127100211\",\"CUST_NAME\" : \"＊＊飞\",\"PROD_MAIN_FLAG\" : \"1\",\"PROD_PRC_NAME\" : \"任我用个人版128元（2018版）\",\"ADDPROD_PRC_NAME\" : \"家庭计划套餐1元（包100分钟）\",\"MEMBER_ROLE_TYPE\" : \"D1\",\"STATE\" : \"A\",\"OBJECT_TYPE\" : \"1\",\"MEMBER_ID\" : \"10031748064\",\"MEMBER_ROLE_NAME\" : \"普通\",\"ADDPROD_PRCID\" : \"PR060276\",\"EXP_DATE\" : \"20991231235959\",\"SHORT_NUM\" : \"525\",\"CUST_ID\" : \"21000000413811\",\"PROD_ID\" : \"CP18W8\",\"ADDPRODPRCINS_ID\" : 60000654282891,\"PROD_PRCID\" : \"CP18W804\",\"CARD_TYPE\" : \"1\",\"SHORTNUM_FLAG\" : \"\",\"MEMBER_ROLE_ID\" : \"10001\",\"BRAND_ID\" : \"002\",\"CARD_NAME\" : \"普通客户\",\"MIN_AMOUNT\" : \"0\",\"REGION_ID\" : \"21\",\"BILL_VALUE\" : \"N\",\"TEAM_ID\" : \"0\",\"MEMBER_DESC\" : \"member_desc\",\"LOCK_FLAG\" : \"0\",\"MEMBER_ROLE_DESC\" : \"家庭计划普通角色\",\"MEM_PROD_LIST\" : {\"MEM_PROD\" : {\"MEMPRODPRCINS_ID\" : \"60000654282891\",\"MEMPROD_NAME\" : \"家庭计划\",\"MEMPROD_PRC_NAME\" : \"家庭计划套餐1元（包100分钟）\",\"MEMPRC_EFF_DATE\" : \"20130127100211\",\"MEMPRC_EXP_DATE\" : \"20991231235959\",\"MASTER_SERV_ID\" : \"1001\",\"MEMCANCEL_DATE\" : \"20181231235959\",\"MEMPROD_MAIN_FLAG\" : \"1\",\"ELEMENT_ID\" : \"1057\",\"MEMPROD_ID\" : \"J0052\",\"MEMPROD_PRCID\" : \"PR060276\",\"NOW_EFF\" : \"1\"}},\"ELEMENT_ID\" : \"1057\",\"CONTRACT_NO\" : \"21100000406092\"}, {\"ADDPROD_NAME\" : \"家庭计划\",\"PRC_EFF_DATE\" : \"20130129082352\",\"PHONE_NO1\" : \"15955740616\",\"IF_DHDX\" : \"0\",\"MsAX_AMOUNT\" : \"999\",\"OBJECT_ID\" : \"21310011187581\",\"BRAND_NAME\" : \"神州行\",\"ADDPROD_ID\" : \"J0052\",\"PHONE_NO\" : \"15955740616\",\"CANCEL_DATE\" : \"20181231235959\",\"MASTER_SERV_ID\" : \"1001\",\"PROD_NAME\" : \"飞享套餐\",\"LEAVE_FLAG\" : \"N\",\"BILL_TYPE\" : \"不统付任何费用\",\"GROUP_ID\" : \"0\",\"PRC_EXP_DATE\" : \"20991231235959\",\"RUN_NAME\" : \"正常\",\"EFF_DATE\" : \"20130129082352\",\"CUST_NAME\" : \"＊＊军\",\"PROD_MAIN_FLAG\" : \"1\",\"PROD_PRC_NAME\" : \"4G飞享套餐8元\",\"ADDPROD_PRC_NAME\" : \"家庭计划2元套餐(包200分钟)\",\"MEMBER_ROLE_TYPE\" : \"D1\",\"STATE\" : \"A\",\"OBJECT_TYPE\" : \"1\",\"MEMBER_ID\" : \"10031799790\",\"MEMBER_ROLE_NAME\" : \"普通\",\"ADDPROD_PRCID\" : \"PR060240\",\"EXP_DATE\" : \"20991231235959\",\"SHORT_NUM\" : \"526\",\"CUST_ID\" : \"21000000372283\",\"PROD_ID\" : \"CP18W3\",\"ADDPRODPRCINS_ID\" : 60000655105223,\"PROD_PRCID\" : \"CP04G138\",\"CARD_TYPE\" : \"1\",\"SHORTNUM_FLAG\" : \"\",\"MEMBER_ROLE_ID\" : \"10001\",\"BRAND_ID\" : \"003\",\"CARD_NAME\" : \"普通客户\",\"MIN_AMOUNT\" : \"0\",\"REGION_ID\" : \"21\",\"BILL_VALUE\" : \"N\",\"TEAM_ID\" : \"0\",\"MEMBER_DESC\" : \"   \",\"LOCK_FLAG\" : \"0\",\"MEMBER_ROLE_DESC\" : \"家庭计划普通角色\",\"MEM_PROD_LIST\" : {\"MEM_PROD\" : {\"MEMPRODPRCINS_ID\" : \"60000655105223\",\"MEMPROD_NAME\" : \"家庭计划\",\"MEMPROD_PRC_NAME\" : \"家庭计划2元套餐(包200分钟)\",\"MEMPRC_EFF_DATE\" : \"20130129082352\",\"MEMPRC_EXP_DATE\" : \"20991231235959\",\"MASTER_SERV_ID\" : \"1001\",\"MEMCANCEL_DATE\" : \"20181231235959\",\"MEMPROD_MAIN_FLAG\" : \"1\",\"ELEMENT_ID\" : \"1057\",\"MEMPROD_ID\" : \"J0052\",\"MEMPROD_PRCID\" : \"PR060240\",\"NOW_EFF\" : \"1\"}},\"ELEMENT_ID\" : \"1057\",\"CONTRACT_NO\" : \"21410003668199\"}, {\"ADDPROD_NAME\" : \"家庭计划\",\"PRC_EFF_DATE\" : \"20161222154016\",\"PHONE_NO1\" : \"13955740838\",\"IF_DHDX\" : \"1\",\"MsAX_AMOUNT\" : \"999\",\"OBJECT_ID\" : \"21060709194332\",\"BRAND_NAME\" : \"动感地带\",\"ADDPROD_ID\" : \"J0052\",\"PHONE_NO\" : \"13955740838\",\"CANCEL_DATE\" : \"20181231235959\",\"MASTER_SERV_ID\" : \"1001\",\"PROD_NAME\" : \"任我用套餐2\",\"LEAVE_FLAG\" : \"N\",\"BILL_TYPE\" : \"不统付任何费用\",\"GROUP_ID\" : \"0\",\"PRC_EXP_DATE\" : \"20991231235959\",\"RUN_NAME\" : \"正常\",\"EFF_DATE\" : \"20161222154016\",\"CUST_NAME\" : \"＊＊莉\",\"PROD_MAIN_FLAG\" : \"1\",\"PROD_PRC_NAME\" : \"任我用198元（2018版）\",\"ADDPROD_PRC_NAME\" : \"家庭计划套餐1元（包100分钟）\",\"MEMBER_ROLE_TYPE\" : \"D1\",\"STATE\" : \"A\",\"OBJECT_TYPE\" : \"1\",\"MEMBER_ID\" : \"10080381925\",\"MEMBER_ROLE_NAME\" : \"普通\",\"ADDPROD_PRCID\" : \"PR060276\",\"EXP_DATE\" : \"20991231235959\",\"SHORT_NUM\" : \"523\",\"CUST_ID\" : \"21000000050519\",\"PROD_ID\" : \"CP18W8\",\"ADDPRODPRCINS_ID\" : 60002113895659,\"PROD_PRCID\" : \"CP18W808\",\"CARD_TYPE\" : \"1\",\"SHORTNUM_FLAG\" : \"\",\"MEMBER_ROLE_ID\" : \"10001\",\"BRAND_ID\" : \"002\",\"CARD_NAME\" : \"普通客户\",\"MIN_AMOUNT\" : \"0\",\"REGION_ID\" : \"21\",\"BILL_VALUE\" : \"N\",\"TEAM_ID\" : \"0\",\"MEMBER_DESC\" : \"   \",\"LOCK_FLAG\" : \"0\",\"MEMBER_ROLE_DESC\" : \"家庭计划普通角色\",\"MEM_PROD_LIST\" : {\"MEM_PROD\" : {\"MEMPRODPRCINS_ID\" : \"60002113895659\",\"MEMPROD_NAME\" : \"家庭计划\",\"MEMPROD_PRC_NAME\" : \"家庭计划套餐1元（包100分钟）\",\"MEMPRC_EFF_DATE\" : \"20161222154016\",\"MEMPRC_EXP_DATE\" : \"20991231235959\",\"MASTER_SERV_ID\" : \"1001\",\"MEMCANCEL_DATE\" : \"20181231235959\",\"MEMPROD_MAIN_FLAG\" : \"1\",\"ELEMENT_ID\" : \"1057\",\"MEMPROD_ID\" : \"J0052\",\"MEMPROD_PRCID\" : \"PR060276\",\"NOW_EFF\" : \"1\"}},\"ELEMENT_ID\" : \"1057\",\"CONTRACT_NO\" : \"21060709194332\"}, {\"ADDPROD_NAME\" : \"家庭计划\",\"PRC_EFF_DATE\" : \"20161222154112\",\"PHONE_NO1\" : \"15955763181\",\"IF_DHDX\" : \"1\",\"MsAX_AMOUNT\" : \"999\",\"OBJECT_ID\" : \"21060914587371\",\"BRAND_NAME\" : \"神州行\",\"ADDPROD_ID\" : \"J0052\",\"PHONE_NO\" : \"15955763181\",\"CANCEL_DATE\" : \"20181231235959\",\"MASTER_SERV_ID\" : \"1001\",\"PROD_NAME\" : \"套餐(58、88)\",\"LEAVE_FLAG\" : \"N\",\"BILL_TYPE\" : \"不统付任何费用\",\"GROUP_ID\" : \"0\",\"PRC_EXP_DATE\" : \"20991231235959\",\"RUN_NAME\" : \"正常\",\"EFF_DATE\" : \"20161222154112\",\"CUST_NAME\" : \"＊＊亮\",\"PROD_MAIN_FLAG\" : \"1\",\"PROD_PRC_NAME\" : \"4G商旅套餐2014版58档\",\"ADDPROD_PRC_NAME\" : \"家庭计划套餐1元（包100分钟）\",\"MEMBER_ROLE_TYPE\" : \"D1\",\"STATE\" : \"A\",\"OBJECT_TYPE\" : \"1\",\"MEMBER_ID\" : \"10080381937\",\"MEMBER_ROLE_NAME\" : \"普通\",\"ADDPROD_PRCID\" : \"PR060276\",\"EXP_DATE\" : \"20991231235959\",\"SHORT_NUM\" : \"528\",\"CUST_ID\" : \"21000000229060\",\"PROD_ID\" : \"CP18W2\",\"ADDPRODPRCINS_ID\" : 60002113895762,\"PROD_PRCID\" : \"P1019108\",\"CARD_TYPE\" : \"1\",\"SHORTNUM_FLAG\" : \"\",\"MEMBER_ROLE_ID\" : \"10001\",\"BRAND_ID\" : \"003\",\"CARD_NAME\" : \"普通客户\",\"MIN_AMOUNT\" : \"0\",\"REGION_ID\" : \"21\",\"BILL_VALUE\" : \"N\",\"TEAM_ID\" : \"0\",\"MEMBER_DESC\" : \"   \",\"LOCK_FLAG\" : \"0\",\"MEMBER_ROLE_DESC\" : \"家庭计划普通角色\",\"MEM_PROD_LIST\" : {\"MEM_PROD\" : {\"MEMPRODPRCINS_ID\" : \"60002113895762\",\"MEMPROD_NAME\" : \"家庭计划\",\"MEMPROD_PRC_NAME\" : \"家庭计划套餐1元（包100分钟）\",\"MEMPRC_EFF_DATE\" : \"20161222154112\",\"MEMPRC_EXP_DATE\" : \"20991231235959\",\"MASTER_SERV_ID\" : \"1001\",\"MEMCANCEL_DATE\" : \"20181231235959\",\"MEMPROD_MAIN_FLAG\" : \"1\",\"ELEMENT_ID\" : \"1057\",\"MEMPROD_ID\" : \"J0052\",\"MEMPROD_PRCID\" : \"PR060276\",\"NOW_EFF\" : \"1\"}},\"ELEMENT_ID\" : \"1057\",\"CONTRACT_NO\" : \"21060914587371\"}, {\"ADDPROD_NAME\" : \"家庭计划\",\"PRC_EFF_DATE\" : \"20170108163251\",\"PHONE_NO1\" : \"18405578477\",\"IF_DHDX\" : \"1\",\"MsAX_AMOUNT\" : \"999\",\"OBJECT_ID\" : \"21310017217542\",\"BRAND_NAME\" : \"神州行\",\"ADDPROD_ID\" : \"J0052\",\"PHONE_NO\" : \"18405578477\",\"CANCEL_DATE\" : \"20181231235959\",\"MASTER_SERV_ID\" : \"1001\",\"PROD_NAME\" : \"飞享套餐\",\"LEAVE_FLAG\" : \"N\",\"BILL_TYPE\" : \"不统付任何费用\",\"GROUP_ID\" : \"0\",\"PRC_EXP_DATE\" : \"20991231235959\",\"RUN_NAME\" : \"正常\",\"EFF_DATE\" : \"20170108163251\",\"CUST_NAME\" : \"＊＊玉\",\"PROD_MAIN_FLAG\" : \"1\",\"PROD_PRC_NAME\" : \"4G飞享套餐8元\",\"ADDPROD_PRC_NAME\" : \"家庭计划套餐1元（包100分钟）\",\"MEMBER_ROLE_TYPE\" : \"D1\",\"STATE\" : \"A\",\"OBJECT_TYPE\" : \"1\",\"MEMBER_ID\" : \"10081546943\",\"MEMBER_ROLE_NAME\" : \"普通\",\"ADDPROD_PRCID\" : \"PR060276\",\"EXP_DATE\" : \"20991231235959\",\"SHORT_NUM\" : \"527\",\"CUST_ID\" : \"21320003059276\",\"PROD_ID\" : \"CP18W3\",\"ADDPRODPRCINS_ID\" : 60002189572602,\"PROD_PRCID\" : \"CP04G138\",\"CARD_TYPE\" : \"1\",\"SHORTNUM_FLAG\" : \"\",\"MEMBER_ROLE_ID\" : \"10001\",\"BRAND_ID\" : \"003\",\"CARD_NAME\" : \"普通客户\",\"MIN_AMOUNT\" : \"0\",\"REGION_ID\" : \"21\",\"BILL_VALUE\" : \"N\",\"TEAM_ID\" : \"0\",\"MEMBER_DESC\" : \"   \",\"LOCK_FLAG\" : \"0\",\"MEMBER_ROLE_DESC\" : \"家庭计划普通角色\",\"MEM_PROD_LIST\" : {\"MEM_PROD\" : {\"MEMPRODPRCINS_ID\" : \"60002189572602\",\"MEMPROD_NAME\" : \"家庭计划\",\"MEMPROD_PRC_NAME\" : \"家庭计划套餐1元（包100分钟）\",\"MEMPRC_EFF_DATE\" : \"20170108163251\",\"MEMPRC_EXP_DATE\" : \"20991231235959\",\"MASTER_SERV_ID\" : \"1001\",\"MEMCANCEL_DATE\" : \"20181231235959\",\"MEMPROD_MAIN_FLAG\" : \"1\",\"ELEMENT_ID\" : \"1057\",\"MEMPROD_ID\" : \"J0052\",\"MEMPROD_PRCID\" : \"PR060276\",\"NOW_EFF\" : \"1\"}},\"ELEMENT_ID\" : \"1057\",\"CONTRACT_NO\" : \"21410009955257\"}],\"SUM_CNT\" : \"19\",\"GROUP_ID\" : \"10010805832\",\"PAR_PROD_ID\" : \"CPX019\",\"GRPID_NO\" : \"21310013625241\",\"MARSTER_NUM\" : \"13655574877\",\"GROUP_TYPE\" : \"D1\",\"GROUP_DESC\" : \"＊＊军\",\"NOW_CNT\" : \"8\",\"USEFLAG\" : \"N\",\"ALLOW_CNT\" : \"11\",\"LOCK_FLAG\" : \"0\",\"MAINMEMPROD_PRCID\" : \"PR060276\",\"PAR_PROD_PRCID\" : \"PX000019\",\"GROUP_CODE\" : \"13655574877\"}}},\"RETURN_CODE\" : \"0\",\"USER_MSG\" : \"OK\",\"RUN_IP\" : \"152.56.136.141\",\"DETAIL_MSG\" : \"OK\"}}";
             jsonObject = JsonHelp.json2Obj(json);
        }catch(Exception e){
             logger.error("查询V网群信息 失败",e);
             return ret.setFail("查询V网群信息 失败");
        }
      
        if(NonUtil.isNon(jsonObject)){
             return ret.setFail("查询V网群信息 失败");
        }
        
        if( ! BossUtil.successCode(JsonUtil.getString(jsonObject, "ROOT.RETURN_CODE"))){   
            String retmsg=JsonUtil.getString(jsonObject, "ROOT.RETURN_MSG");
            return ret.setFail(NonUtil.isNon(retmsg)?"查询V网群信息 失败":retmsg);
        }
        
        return ret.setSuccess("OK",jsonObject);
        
    }
    /**
     * 短号校验 sHomeShtNumChk
     * @param map
     * @param u
     * @return
     */
    public static ReturnValueDomain<JSONObject> sHomeShtNumChk(Map map , SellstdUserext u){
        ReturnValueDomain<JSONObject> ret=new ReturnValueDomain<JSONObject>();
        Map<String,Object> param=new HashMap<String,Object>();
        Map<String,Object> root=new HashMap<String,Object>();
        Object common_info=CommonTool.getCommon_info("1486",u.getBoss_no() );
        Object opr_info=CommonTool.getOpr_info(u.getChanneltype(), u.getGroup_id(), u.getBoss_no(), "1486", u.getRegin_id());
        root.put("COMMON_INFO", common_info);
        root.put("OPR_INFO", opr_info);
        root.put("GROUP_ID", map.get("group_id"));
        root.put("PHONE_NO", map.get("grp_phone"));
        Map<String,Object> shortnum_list=new HashMap<String,Object>();
        root.put("SHORTNUM_LIST", shortnum_list);
        Map<String,Object> shortnum_info=new HashMap<String,Object>();
        shortnum_list.put("SHORTNUM_INFO", shortnum_info);
        shortnum_info.put("SHORT_NUM", map.get("short_num"));
        param.put("ROOT", root);
        String paramstr= JSONObject.toJSONString(param);
        ReqUrlConfig ReqUrlConfig=BeanUtil.getBean("reqUrlConfig", ReqUrlConfig.class);
        String http_url=ReqUrlConfig.getEsbUrl();
        String method = "sHomeShtNumChk";
        JSONObject jsonObject=null;
        try{
             ReturnValueDomain<String> esb_ret =  CommonEsbUtil.sendPostRestful(http_url+method,paramstr);
             if(esb_ret.hasFail()||NonUtil.isNon(esb_ret.getObject())){
                 return ret.setFail("查询用户身份校验失败");
             }
             String json = esb_ret.getObject();
            //test
           // String json = "{\"ROOT\":{\"DETAIL_MSG\":\"OK\",\"OUT_DATA\":{\"CHK_FLAG\":\"Y\",\"CHK_NOTES\":\"短号校验通过\"},\"PROMPT_MSG\":\"\",\"RETURN_CODE\":\"0\",\"RETURN_MSG\":\"OK\",\"RUN_IP\":\"152.56.136.141\",\"USER_MSG\":\"OK\"}}";
            jsonObject = JsonHelp.json2Obj(json);
        }catch(Exception e){
            logger.error("短号校验失败",e);
            return ret.setFail("短号校验信息 失败");
        }
        
        if(NonUtil.isNon(jsonObject)){
            return ret.setFail("短号校验失败");
        }
        
        if( ! BossUtil.successCode(JsonUtil.getString(jsonObject, "ROOT.RETURN_CODE"))){   
            String retmsg=JsonUtil.getString(jsonObject, "ROOT.RETURN_MSG");
            return ret.setFail(NonUtil.isNon(retmsg)?"短号校验失败":retmsg);
        }
        
        return ret.setSuccess("OK",jsonObject);
        
    }
    
    /**
     * 变更提交校验 sTOM_vNetChgCfm
     * @param map
     * @param u
     * @return
     */
    public static ReturnValueDomain<JSONObject> sTOM_vNetChgCfm(Map map , SellstdUserext u){    	
        ReturnValueDomain<JSONObject> ret=new ReturnValueDomain<JSONObject>();
        Map<String,Object> param=new HashMap<String,Object>();
        Map<String,Object> root=new HashMap<String,Object>();
        root.put("AUTO_CONFIRM", "Y");
        root.put("PHONE_NO", map.get("grp_phone"));
        root.put("OP_NOTE", "");
        root.put("IS_WEB", "N");
        root.put("NEW_BUSI", "N");
        root.put("GROUPID_NO",  map.get("grp_id_no"));
        Map<String,Object> groupmbr_list=new HashMap<String,Object>();
        root.put("GROUPMBR_LIST", groupmbr_list);
        List<Map<String,Object>> groupmbr = new ArrayList<Map<String,Object>>();
        groupmbr_list.put("GROUPMBR", groupmbr);
        Map<String,Object> groupmbr_map=new HashMap<String,Object>(); 
        groupmbr.add(groupmbr_map);
        
        groupmbr_map.put("ACCT_ITEMS", map.get("acct_items"));
        /*groupmbr_map.put("EFF_DATE", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));//当前时间
        groupmbr_map.put("EXP_DATE", "20990131235959");*/
        groupmbr_map.put("EFF_DATE", map.get("eff_date"));
        groupmbr_map.put("EXP_DATE", map.get("exp_date"));
        groupmbr_map.put("MEMBER_ID", map.get("member_id"));
        groupmbr_map.put("MEMBER_OP_TYPE", map.get("member_op_type"));
        groupmbr_map.put("MEMBER_ROLE_ID", map.get("member_role_id"));
        groupmbr_map.put("OBJECT_PHONE", map.get("object_phone"));
        groupmbr_map.put("PROD_ID", map.get("prod_id"));
        groupmbr_map.put("PROD_PRCID", map.get("prod_prcid"));
        groupmbr_map.put("SHORT_NUM", map.get("short_num"));
        groupmbr_map.put("SEC_CONFIRM", map.get("sec_confirm"));//成员反向加群校验
        if("A".equals(map.get("member_op_type"))){//成员添加
            //groupmbr_map.put("SEC_CONFIRM", "Y");
            groupmbr_map.put("IFKT_DHDX","1");
        }
        if("D".equals(map.get("member_op_type"))&&"PC".equals(map.get("member_op_type"))){//删除成员和离群
            groupmbr_map.put("IFKT_DHDX","0");
        }
        Map<String,Object> opr_info=new HashMap<String,Object>(); 
        root.put("OPR_INFO", opr_info);
        opr_info.put("AUTHEN_CODE", map.get("authen_code"));
        if ("3".equals(map.get("AUTHEN_CODE"))) {
            opr_info.put("AUTHEN_NAME", "验证码加上行回复");
        } else if ("4".equals(map.get("authen_code"))) {
            opr_info.put("AUTHEN_NAME", "服务密码");
        } else if ("23".equals(map.get("authen_code"))) {
            opr_info.put("AUTHEN_NAME", "二代身份证读取");
        } else {
            opr_info.put("AUTHEN_NAME", "");
        }
        opr_info.put("CHANNEL_TYPE", "431");
        opr_info.put("CONTACT_ID", "1120180150000001471605836");
        opr_info.put("GROUP_ID", u.getGroup_id());
        
        if(NonUtil.isNotNon(map.get("myoptype")) && "delMember".equals(map.get("myoptype"))){
            opr_info.put("ID_NO", map.get("id_no"));
        }else{
            opr_info.put("ID_NO", map.get("main_id_no") );
        }
        
        opr_info.put("IP_ADDRESS",MemberUtil.getIPAddress() );
        opr_info.put("IS_WEB", "N");
        opr_info.put("LOGIN_NO", u.getBoss_no());
        opr_info.put("LOGIN_PWD", "fb0bdec823cf4cdb");
        opr_info.put("MASTER_SERV_ID", "1001");
        opr_info.put("OP_CODE", "1486");
        opr_info.put("REGION_ID", u.getRegin_id());
        opr_info.put("SERVICE_NO", map.get("service_no"));
        
        param.put("ROOT", root);
        String paramstr= JSONObject.toJSONString(param);
        ReqUrlConfig ReqUrlConfig=BeanUtil.getBean("reqUrlConfig", ReqUrlConfig.class);
        String http_url=ReqUrlConfig.getEsbUrl();
        String method = "sTOM_vNetChgCfm";
        JSONObject jsonObject=null;
        try{
             ReturnValueDomain<String> esb_ret =  CommonEsbUtil.sendPostRestful(http_url+method,paramstr);
             if(esb_ret.hasFail()||NonUtil.isNon(esb_ret.getObject())){
                 return ret.setFail("变更提交失败");
             }
             String json = esb_ret.getObject();
            //test
            //String json = "{\"ROOT\":{\"CART_ID\":\"\",\"CNTT_LOGIN_ACCEPT\":\"30002328340128\",\"CONTACT_ID\":\"1120190150000002214778208\",\"CREATE_ACCEPT\":\"30002328340128\",\"DETAIL_MSG\":\"OK\",\"ORDER_ID\":\"O19010300000487\",\"OUT_DATA\":{},\"PROMPT_MSG\":\"\",\"RETURN_CODE\":\"0\",\"RETURN_MSG\":\"OK\",\"RUN_IP\":\"152.56.136.141\",\"SERVICE_NO\":\"11310013268239\",\"SUB_ORDER_ID\":\"S19010300000496\",\"USER_MSG\":\"OK\"}}";
            jsonObject = JsonHelp.json2Obj(json);
            jsonObject.put("inputxml", paramstr);
        }catch(Exception e){
            logger.error("变更提交失败",e);
            return ret.setFail("变更提交 失败");
        }
        
        if(NonUtil.isNon(jsonObject)){
            return ret.setFail("短号校验失败");
        }
        
        if( ! BossUtil.successCode(JsonUtil.getString(jsonObject, "ROOT.RETURN_CODE"))){   
            String retmsg=JsonUtil.getString(jsonObject, "ROOT.RETURN_MSG");
            return ret.setFail(NonUtil.isNon(retmsg)?"变更提交失败":retmsg);
        }
        
        return ret.setSuccess("OK",jsonObject);
        
    }
    
    /**
     * 开户提交校验 sPubOpenUser
     * @param map
     * @param u
     * @return
     */
    public static ReturnValueDomain<JSONObject> sPubOpenUser(Map map , SellstdUserext u){
        ReturnValueDomain<JSONObject> ret=new ReturnValueDomain<JSONObject>();
        Map<String,Object> param=new HashMap<String,Object>();
        Map<String,Object> root=new HashMap<String,Object>();
        
        Object common_info=CommonTool.getCommon_info("1486",u.getBoss_no() );
        root.put("AUTO_CONFIRM", "N");
        root.put("COMMON_INFO", common_info);
        root.put("EMP_ID", "");
        //主角
        Map<String,Object> groupmbr_list=new HashMap<String,Object>();
        List<Map<String,Object>> groupmbr = new ArrayList<Map<String,Object>>();
        groupmbr_list.put("GROUPMBR", groupmbr);
        Map<String,Object> zj_groupmbr_map=new HashMap<String,Object>(); 
        groupmbr.add(zj_groupmbr_map);
        zj_groupmbr_map.put("ACCT_ITEMS", map.get("zj_acct_items"));
        zj_groupmbr_map.put("IFKT_DHDX","1");
        zj_groupmbr_map.put("MEMBER_ROLE_ID", "10002");
        zj_groupmbr_map.put("OBJECT_PHONE", map.get("zj_object_phone"));        
        zj_groupmbr_map.put("PROD_PRCID", map.get("zj_prod_prcid"));
        zj_groupmbr_map.put("SHORT_NUM", map.get("zj_short_num"));
        
        //普通
        Map<String,Object> pt_groupmbr_map=new HashMap<String,Object>(); 
        groupmbr.add(pt_groupmbr_map);
        pt_groupmbr_map.put("ACCT_ITEMS", map.get("pt_acct_items"));
        pt_groupmbr_map.put("IFKT_DHDX","1");
        pt_groupmbr_map.put("MEMBER_ROLE_ID", "10001");
        pt_groupmbr_map.put("OBJECT_PHONE", map.get("pt_object_phone"));        
        pt_groupmbr_map.put("PROD_PRCID", map.get("pt_prod_prcid"));
        pt_groupmbr_map.put("SHORT_NUM", map.get("pt_short_num"));
        
        
        root.put("GROUPMBR_LIST", groupmbr_list);
        root.put("GROUP_ID", u.getGroup_id());
        root.put("IS_WEB", "N");
        root.put("LOGIN_NO", u.getBoss_no());
        root.put("MASTER_SERV_ID", "1001");
        root.put("NEW_BUSI", "N");
        
        Map<String,Object> opr_info=new HashMap<String,Object>();
        opr_info.put("AUTHEN_CODE", map.get("authen_code"));
        if ("3".equals(map.get("AUTHEN_CODE"))) {
            opr_info.put("AUTHEN_NAME", "验证码加上行回复");
        } else if ("4".equals(map.get("authen_code"))) {
            opr_info.put("AUTHEN_NAME", "服务密码");
        } else if ("23".equals(map.get("authen_code"))) {
            opr_info.put("AUTHEN_NAME", "二代身份证读取");
        } else {
            opr_info.put("AUTHEN_NAME", "");
        }
        opr_info.put("CHANNEL_TYPE", "431");
        opr_info.put("CONTACT_ID", "1120181250000002214780758");
        opr_info.put("GROUP_ID", u.getGroup_id());
        opr_info.put("IP_ADDRESS", MemberUtil.getIPAddress());
        opr_info.put("LOGIN_NO", u.getBoss_no());
        opr_info.put("LOGIN_PWD", "fb0bdec823cf4cdb");
        opr_info.put("OP_CODE", "1486");
        opr_info.put("REGION_ID", u.getRegin_id());
        opr_info.put("REL_OP_CODE", "1486");
        
        root.put("OPR_INFO", opr_info);
        root.put("OP_CODE", "1486");
        root.put("OP_NOTE", map.get("op_note"));
        root.put("PHONE_NO", map.get("phoneno"));
        root.put("PROD_ID", map.get("prod_id"));
        root.put("PROD_PRCID", map.get("prod_prcid"));
        root.put("REGION_ID", u.getRegin_id());
        root.put("SALE_LOGIN_NAME", "");
        root.put("SALE_LOGIN_NO", "");
        root.put("SERVICE_NO", map.get("service_no"));
        root.put("USER_PASSWD", "123123");
        root.put("IS_CURRENT_EXPDATE", map.get("is_current_expdate"));//IS_CURRENT_EXPDATE 字段，月底失效的成员的时候返回，其值为Y
        
        param.put("ROOT", root);
        String paramstr= JSONObject.toJSONString(param);
        ReqUrlConfig ReqUrlConfig=BeanUtil.getBean("reqUrlConfig", ReqUrlConfig.class);
        String http_url=ReqUrlConfig.getEsbUrl();
        String method = "sPubOpenUser";
        JSONObject jsonObject=null;
        try{
             ReturnValueDomain<String> esb_ret =  CommonEsbUtil.sendPostRestful(http_url+method,paramstr);
             if(esb_ret.hasFail()||NonUtil.isNon(esb_ret.getObject())){
                 return ret.setFail("开户提交失败");
             }
             String json = esb_ret.getObject();
            //test
            //String json = "{\"ROOT\":{\"DETAIL_MSG\": \"OK\",\"OUT_DATA\":{\"CART_ID\": \"\",\"CNTT_LOGIN_ACCEPT\": \"30002328346049\",\"CUST_ID\": \"11002600054795\",\"ID_NO\": \"11310037890100\",\"ORDER_ID\": \"O18122700000214\",\"SERVICE_NO\": \"13505612523\",\"SUB_ORDER_ID\": \"S18122700000226\"},\"PROMPT_MSG\": \"\",\"RETURN_CODE\": \"0\",\"RETURN_MSG\": \"OK\",\"RUN_IP\": \"152.56.136.141\",\"USER_MSG\": \"OK\"}}";
             jsonObject = JsonHelp.json2Obj(json);
             jsonObject.put("inputxml", paramstr);
        }catch(Exception e){
            logger.error("开户提交失败",e);
            return ret.setFail("开户提交失败");
        }
        
        if(NonUtil.isNon(jsonObject)){
            return ret.setFail("开户校验失败");
        }
        
        if( ! BossUtil.successCode(JsonUtil.getString(jsonObject, "ROOT.RETURN_CODE"))){   
            String retmsg=JsonUtil.getString(jsonObject, "ROOT.RETURN_MSG");
            return ret.setFail(NonUtil.isNon(retmsg)?"开户提交失败":retmsg);
        }
        
        return ret.setSuccess("OK",jsonObject);
        
    }
    
    /**
     * sQryCompMbr
     * @param map
     * @param u
     * @return
     */
    public static ReturnValueDomain<JSONObject> sQryCompMbr(Map map , SellstdUserext u){
        ReturnValueDomain<JSONObject> ret=new ReturnValueDomain<JSONObject>();
        Map<String,Object> param=new HashMap<String,Object>();
        Map<String,Object> root=new HashMap<String,Object>();
        
        Object common_info=CommonTool.getCommon_info("1486",u.getBoss_no() );
        root.put("COMMON_INFO", common_info);
        root.put("PHONE_NO", map.get("grp_phone"));
        root.put("ID_NO", map.get("grp_id_no"));
        
        Map<String,Object> request_info=new HashMap<String,Object>();
        Map<String,Object> busi_info_list=new HashMap<String,Object>();
        request_info.put("BUSI_INFO_LIST", busi_info_list);
        Map<String,Object> busi_info=new HashMap<String,Object>();
        busi_info_list.put("BUSI_INFO", busi_info);
        busi_info.put("PHONE_NO", map.get("grp_phone"));
        busi_info.put("ID_NO", map.get("grp_id_no"));
        busi_info.put("OTHER_CHK", "Y");
        Map<String,Object> opr_info=new HashMap<String,Object>();
        opr_info.put("AUTHEN_CODE", map.get("authen_code"));
        if ("3".equals(map.get("AUTHEN_CODE"))) {
            opr_info.put("AUTHEN_NAME", "验证码加上行回复");
        } else if ("4".equals(map.get("authen_code"))) {
            opr_info.put("AUTHEN_NAME", "服务密码");
        } else if ("23".equals(map.get("authen_code"))) {
            opr_info.put("AUTHEN_NAME", "二代身份证读取");
        } else {
            opr_info.put("AUTHEN_NAME", "");
        }
        opr_info.put("AUTHEN_CODE", "");
        opr_info.put("AUTHEN_NAME", "服务密码");
        opr_info.put("CHANNEL_TYPE", "431");
        opr_info.put("CONTACT_ID", "1120181250000002214780758");
        opr_info.put("GROUP_ID", u.getGroup_id());
        opr_info.put("IP_ADDRESS", MemberUtil.getIPAddress());
        opr_info.put("LOGIN_NO", u.getBoss_no());
        opr_info.put("LOGIN_PWD", "fb0bdec823cf4cdb");
        opr_info.put("OP_CODE", "1486");
        opr_info.put("REGION_ID", u.getRegin_id());
        opr_info.put("REL_OP_CODE", "1486");
        request_info.put("OPR_INFO", opr_info);

        root.put("REQUEST_INFO", request_info);
        param.put("ROOT", root);
        String paramstr= JSONObject.toJSONString(param);
        ReqUrlConfig ReqUrlConfig=BeanUtil.getBean("reqUrlConfig", ReqUrlConfig.class);
        String http_url=ReqUrlConfig.getEsbUrl();
        String method = "sQryCompMbr";
        JSONObject jsonObject=null;
        try{
            ReturnValueDomain<String> esb_ret =  CommonEsbUtil.sendPostRestful(http_url+method,paramstr);
             if(esb_ret.hasFail()||NonUtil.isNon(esb_ret.getObject())){
                 return ret.setFail("解散群组失败");
             }
             String json = esb_ret.getObject();
             //test
             //String json = "{\"ROOT\":{\"DETAIL_MSG\":\"OK\",\"OUT_DATA\":{\"CONTRACT_NO\":11410012566264,\"GROUP_ID\":10011124752,\"GROUP_NAME\":\"家庭计划群\",\"GROUP_TYPE\":\"D1\",\"LIMIT_FLAG\":\"0\",\"UNPACK_FLAG\":\"Y\",\"USERDEAL_FLAG\":\"1055\"},\"PROMPT_MSG\":\"\",\"RETURN_CODE\":\"0\",\"RETURN_MSG\":\"OK\",\"RUN_IP\":\"152.56.136.141\",\"USER_MSG\":\"OK\"}}";
             jsonObject = JsonHelp.json2Obj(json);
        }catch(Exception e){
            logger.error("解散群组失败",e);
            return ret.setFail("解散群组失败");
        }
        
        if(NonUtil.isNon(jsonObject)){
            return ret.setFail("解散群组失败");
        }
        String retmsg=JsonUtil.getString(jsonObject, "ROOT.RETURN_MSG");
        if( ! BossUtil.successCode(JsonUtil.getString(jsonObject, "ROOT.RETURN_CODE"))){   
            return ret.setFail(retmsg);
        }
        
        if(!"Y".equals(JsonUtil.getString(jsonObject, "ROOT.OUT_DATA.UNPACK_FLAG"))){
            return ret.setFail("解散群组失败");
        }
        return ret.setSuccess("OK",jsonObject);
        
    }
    /**
     * sTOM_UnPackCfm
     * @param map
     * @param u
     * @return
     */
    public static ReturnValueDomain<JSONObject> sTOM_UnPackCfm(Map map , SellstdUserext u){
        ReturnValueDomain<JSONObject> ret=new ReturnValueDomain<JSONObject>();
        Map<String,Object> param=new HashMap<String,Object>();
        Map<String,Object> root=new HashMap<String,Object>();
        
        Object common_info=CommonTool.getCommon_info("1486",u.getBoss_no() );
        root.put("COMMON_INFO", common_info);
        root.put("AUTO_CONFIRM", "Y");
        root.put("PHONE_NO", map.get("grp_phone"));
        root.put("IS_WEB", "N");
        root.put("NEW_BUSI", "N");
        root.put("GROUPID_NO",  map.get("grp_id_no"));
        
        
        Map<String,Object> opr_info=new HashMap<String,Object>();
        opr_info.put("AUTHEN_CODE", map.get("authen_code"));
        if ("3".equals(map.get("AUTHEN_CODE"))) {
            opr_info.put("AUTHEN_NAME", "验证码加上行回复");
        } else if ("4".equals(map.get("authen_code"))) {
            opr_info.put("AUTHEN_NAME", "服务密码");
        } else if ("23".equals(map.get("authen_code"))) {
            opr_info.put("AUTHEN_NAME", "二代身份证读取");
        } else {
            opr_info.put("AUTHEN_NAME", "");
        }
        opr_info.put("BRAND_ID", "0");
        opr_info.put("CHANNEL_TYPE", "431");
        opr_info.put("CONTACT_ID", "1120181250000002214780758");
        opr_info.put("CONTRACT_NO", "0");
        opr_info.put("GROUP_ID", u.getGroup_id());
        opr_info.put("ID_NO", map.get("id_no"));
        opr_info.put("IMPOWER_LOGIN", "");
        opr_info.put("IP_ADDRESS", MemberUtil.getIPAddress());
        opr_info.put("LOGIN_NO", u.getBoss_no());
        opr_info.put("LOGIN_PWD", "fb0bdec823cf4cdb");
        opr_info.put("OP_CODE", "1486");
        opr_info.put("OP_NOTE", "");
        opr_info.put("REGION_ID", u.getRegin_id());
        opr_info.put("REL_OP_CODE", "1486");
        opr_info.put("PRINT_TYPE", "1");
        opr_info.put("MASTER_SERV_ID", "1007");
        opr_info.put("IS_WEB", "N");
        opr_info.put("CONTRACT_NO", "0");
        opr_info.put("SERVICE_NO", map.get("service_no"));
        
        root.put("OPR_INFO", opr_info);

        param.put("ROOT", root);
        String paramstr= JSONObject.toJSONString(param);
        ReqUrlConfig ReqUrlConfig=BeanUtil.getBean("reqUrlConfig", ReqUrlConfig.class);
        String http_url=ReqUrlConfig.getEsbUrl();
        String method = "sTOM_UnPackCfm";
        JSONObject jsonObject=null;
        try{
            ReturnValueDomain<String> esb_ret =  CommonEsbUtil.sendPostRestful(http_url+method,paramstr);
             if(esb_ret.hasFail()||NonUtil.isNon(esb_ret.getObject())){
                 return ret.setFail("解散群组失败");
             }
             String json = esb_ret.getObject();
             jsonObject = JsonHelp.json2Obj(json);
             jsonObject.put("inputxml", paramstr);
        }catch(Exception e){
            logger.error("解散群组失败",e);
            return ret.setFail("解散群组失败");
        }
        String retmsg=JsonUtil.getString(jsonObject, "ROOT.RETURN_MSG");
        if(NonUtil.isNon(jsonObject)){
            return ret.setFail("解散群组失败");
        }
        
        if( ! BossUtil.successCode(JsonUtil.getString(jsonObject, "ROOT.RETURN_CODE"))){   
            return ret.setFail(retmsg);
        }
        
        return ret.setSuccess("OK",jsonObject);
        
    }
}
