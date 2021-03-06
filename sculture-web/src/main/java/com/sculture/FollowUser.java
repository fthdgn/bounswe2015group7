package com.sculture;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Atakan Arıkan on 14.12.2015.
 */
@WebServlet(name = "followuser")
public class FollowUser extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpResponse<JsonNode> jsonResponse = null;
        String requestURL = request.getRequestURL().toString();
        String user_id = requestURL.substring(requestURL.lastIndexOf('/') + 1);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", user_id);
            jsonObject.put("is_follow", true);
            JsonNode jsonNode = new JsonNode(jsonObject.toString());
            jsonResponse = Unirest.post(Const.REST_BASE_URL + Const.Api.USER_FOLLOW)
                    .header("Content-Type", "application/json")
                    .header("access-token", request.getSession().getAttribute("access_token").toString())
                    .body(jsonNode)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        response.sendRedirect("/sculture/get/user/" + user_id);
    }
}
