package cn.stylefeng.guns.modular.system.model;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 通知表
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-07
 */
public class CustomerDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long customerId;
    /**
     * 公司名称
     */
    private String name;
    /**
     * 联系人
     */
    private String contact;
    /**
     * 联系方式
     */
    private String contactTel;
    /**
     * 联系方式
     */
    private String contactTel2;
    /**
     * 联系方式
     */
    private String contactTel3;
    /**
     * 客户基本情况
     */
    private String content;

    /**
     * 跟进状态(字典) 电话一试、二次沟通、约出见面
     */
    private String status;

    //办理的项目49种
    /**
     * 跟进情况
     */
    private String followContent;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private Long createUser;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 修改人
     */
    private Long updateUser;

    private Integer valid;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getContactTel2() {
        return contactTel2;
    }

    public void setContactTel2(String contactTel2) {
        this.contactTel2 = contactTel2;
    }

    public String getContactTel3() {
        return contactTel3;
    }

    public void setContactTel3(String contactTel3) {
        this.contactTel3 = contactTel3;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFollowContent() {
        return followContent;
    }

    public void setFollowContent(String followContent) {
        this.followContent = followContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }
}
