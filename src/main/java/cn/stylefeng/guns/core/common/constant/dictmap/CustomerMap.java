/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.stylefeng.guns.core.common.constant.dictmap;

import cn.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 通知的映射
 *
 * @author fengshuonan
 * @date 2017-05-06 15:01
 */
public class CustomerMap extends AbstractDictMap {

    @Override
    public void init() {
        put("name", "公司名称");
        put("contact", "联系人");
        put("contactTel", "联系方式1");
        put("contactTel2", "联系方式2");
        put("contactTel3", "联系方式3");
        put("projectDict", "参与项目");
        put("content", "客户基本情况");
        put("status", "跟进状态");
        put("followContent", "跟进情况");
    }

    @Override
    protected void initBeWrapped() {
    }
}
