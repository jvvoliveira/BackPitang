package com.movie.pitang.error;

public class ResourceNotFoundExceptionDetail {

    private String title;
    private int status;
    private long momment;
    private String detail;
    private String developerMessage;

    private ResourceNotFoundExceptionDetail(){

    }

    public String getTitle() {
        return title;
    }

    public int getStatus() {
        return status;
    }

    public long getMomment() {
        return momment;
    }

    public String getDetail() {
        return detail;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }


    public static final class ResourceBuilder {
        private String title;
        private int status;
        private long momment;
        private String detail;
        private String developerMessage;

        private ResourceBuilder() {
        }

        public static ResourceBuilder builder() {
            return new ResourceBuilder();
        }

        public ResourceBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ResourceBuilder status(int status) {
            this.status = status;
            return this;
        }

        public ResourceBuilder momment(long momment) {
            this.momment = momment;
            return this;
        }

        public ResourceBuilder detail(String detail) {
            this.detail = detail;
            return this;
        }

        public ResourceBuilder developerMessage(String developerMessage) {
            this.developerMessage = developerMessage;
            return this;
        }

        public ResourceNotFoundExceptionDetail build() {
            ResourceNotFoundExceptionDetail resourceNotFoundExceptionDetail = new ResourceNotFoundExceptionDetail();
            resourceNotFoundExceptionDetail.momment = this.momment;
            resourceNotFoundExceptionDetail.detail = this.detail;
            resourceNotFoundExceptionDetail.title = this.title;
            resourceNotFoundExceptionDetail.developerMessage = this.developerMessage;
            resourceNotFoundExceptionDetail.status = this.status;
            return resourceNotFoundExceptionDetail;
        }
    }
}
