package cn.element.web.servlet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ResponseEntity<T> {

    private String msg;

    private T data;

    private int status;

    public static <T> ResponseEntity<T> ok() {
        return ok(null, null);
    }

    public static <T> ResponseEntity<T> ok(T data) {
        return ok(null, data);
    }

    public static <T> ResponseEntity<T> ok(String msg, T data) {
        ResponseEntity<T> response = new ResponseEntity<>();
        response.setMsg(msg)
                .setData(data)
                .setStatus(200);
        return response;
    }

    public static <T> ResponseEntity<T> badRequest() {
        ResponseEntity<T> response = new ResponseEntity<>();
        response.setMsg("请求不合法")
                .setStatus(400);
        return response;
    }

    public static <T> ResponseEntity<T> notFound() {
        ResponseEntity<T> response = new ResponseEntity<>();
        response.setMsg("数据飞走了~")
                .setStatus(404);
        return response;
    }

    public static <T> ResponseEntity<T> serverError() {
        ResponseEntity<T> response = new ResponseEntity<>();
        response.setMsg("服务器异常~")
                .setStatus(500);
        return response;
    }
}