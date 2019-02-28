package cn.stylefeng.guns.modular.system.model;

import java.io.Serializable;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

/**
 * 发票模板
 * 
 * @author anhao
 *
 */
@ExcelTarget("CustomerSourceTemplate")
public class CustomerSourceTemplate implements Serializable {

	private static final long serialVersionUID = -4507250469314660740L;

    @Excel(name = "公司名称例如：201800001", orderNum = "1", isImportField = "name")
    private String name;

	@Excel(name = "联系方式例如：15901234123,13901234123", orderNum = "2", isImportField = "contactTel")
	private String contactTel;

	@Excel(name = "分配用户例如：张三,李四", orderNum = "3", isImportField = "userName")
	private String userName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
