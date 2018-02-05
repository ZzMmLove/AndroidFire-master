package com.gdgst.shuoke360.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 8/14 0014.
 */

public class Category implements Serializable {


    private List<AttrBean> attr;

    public List<AttrBean> getAttr() {
        return attr;
    }

    public void setAttr(List<AttrBean> attr) {
        this.attr = attr;
    }

    public static class AttrBean {
        /**
         * key : 年级
         * vals : [{"id":"001","isCheck":"fase","val":"三年级上"},{"id":"001","isCheck":"fase","val":"三年级下"},{"id":"001","isCheck":"fase","val":"四年级上"},{"id":"001","isCheck":"fase","val":"四年级下"},{"id":"001","isCheck":"fase","val":"五年级上"},{"id":"001","isCheck":"fase","val":"五年级下"},{"id":"001","isCheck":"fase","val":"六年级上"},{"id":"001","isCheck":"fase","val":"六年级下"},{"id":"001","isCheck":"fase","val":"七年级上"},{"id":"001","isCheck":"fase","val":"七年级下"},{"id":"001","isCheck":"fase","val":"八年级上"},{"id":"001","isCheck":"fase","val":"八年级下"},{"id":"001","isCheck":"fase","val":"九年级上"},{"id":"001","isCheck":"fase","val":"九年级下"}]
         */

        private String key;
        private List<ValsBean> vals;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<ValsBean> getVals() {
            return vals;
        }

        public void setVals(List<ValsBean> vals) {
            this.vals = vals;
        }

        public static class ValsBean {
            /**
             * id : 001
             * isCheck : fase
             * val : 三年级上
             */

            private String id;
            private boolean isCheck;
            private String val;
            private String type;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public boolean getIsCheck() {
                return isCheck;
            }

            public void setIsCheck(boolean isCheck) {
                this.isCheck = isCheck;
            }

            public String getVal() {
                return val;
            }

            public void setVal(String val) {
                this.val = val;
            }

            @Override
            public String toString() {
                return "ValsBean{" +
                        "id='" + id + '\'' +
                        ", isCheck=" + isCheck +
                        ", val='" + val + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "AttrBean{" +
                    "key='" + key + '\'' +
                    ", vals=" + vals +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Category{" +
                "attr=" + attr +
                '}';
    }
}
