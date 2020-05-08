package domain;

/**
 * @author wu
 * @date 2020/4/22 - 17:07
 * 分页类代码封装
 */
public class Page {
    private int start;//查询数据库的起始记录条数
    private int currentPage;//当前页
    private int pageSize;//每页数量

    public Page(int currentPage, int pageSize) {
        this.start=(currentPage-1)*pageSize;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "Page{" +
                "start=" + start +
                ", currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                '}';
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStart() {
        return start;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }
}
