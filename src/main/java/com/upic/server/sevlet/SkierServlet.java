package com.upic.server.sevlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.upic.server.model.LiftRide;
import com.upic.server.model.SkierVertical;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

@WebServlet(
        name = "SkierServlet",
        value = {"/skiers/*"}
)
public class SkierServlet extends HttpServlet {
    private Gson gson = new Gson();
    private ObjectPool<Channel> pool;
    private static final String QUEUE_NAME = "SkierServletPostQueue";

    public SkierServlet() {
    }

    public void init() {
        this.pool = new GenericObjectPool(new ConnectionPoolFactory());
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        String urlPath = req.getPathInfo();
        if (urlPath != null && !urlPath.isEmpty()) {
            String[] urlParts = urlPath.split("/");
            if (!this.isUrlValid(urlParts)) {
                res.setStatus(404);
            } else {
                res.setStatus(200);
                if (urlParts.length == 3) {
                    List<SkierVertical.ResortSeasonTotal> vrl = new ArrayList();
                    vrl.add(new SkierVertical.ResortSeasonTotal("2024", 32));
                    res.getWriter().write(this.gson.toJson(vrl));
                } else {
                    res.getWriter().write("34507");
                }
            }

        } else {
            res.setStatus(404);
            res.getWriter().write(gson.toJson("Missing Parameter"));
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        String urlPath = req.getPathInfo();
        System.out.println("It's here");
        if (urlPath != null && !urlPath.isEmpty()) {
            String[] urlParts = urlPath.split("/");
            if (!this.isUrlValid(urlParts)) {
                res.setStatus(404);
                res.getWriter().write(gson.toJson("Missing Parameter"));
            } else {
                try {
                    StringBuilder sb = new StringBuilder();

                    String s;
                    while((s = req.getReader().readLine()) != null) {
                        sb.append(s);
                    }

                    System.out.println(sb.toString());
                    LiftRide liftRide = (LiftRide)this.gson.fromJson(sb.toString(), LiftRide.class);
                    System.out.println("check point" + liftRide.toString());
                    int skierID = Integer.parseInt(urlParts[7]);
                    JsonObject liftInfo = new JsonObject();
                    liftInfo.addProperty("time", liftRide.getTime());
                    liftInfo.addProperty("liftID", liftRide.getLiftID());
                    liftInfo.addProperty("skierID", skierID);
                    Channel channel = null;

                    try {
                        channel = (Channel)this.pool.borrowObject();
                        channel.queueDeclare("SkierServletPostQueue", false, false, false, (Map)null);
                        channel.basicPublish("", "SkierServletPostQueue", (BasicProperties)null, liftInfo.toString().getBytes());
                    } catch (Exception var20) {
                        throw new RuntimeException("Unable to borrow from pool" + var20.toString());
                    } finally {
                        try {
                            if (channel != null) {
                                System.out.println("channel return Done");
                                this.pool.returnObject(channel);
                            }
                        } catch (Exception var19) {
                            System.out.println("error when returning channel");
                        }

                    }

                    res.setStatus(201);
                } catch (Exception var22) {
                    res.setStatus(404);
                }
            }

        } else {
            res.setStatus(404);
            res.getWriter().write("missing parameters");
        }
    }

    private boolean isUrlValid(String[] urlPath) {
        if (urlPath.length == 3) {
            return urlPath[1].chars().allMatch(Character::isDigit) && urlPath[2].contains("vertical");
        } else if (urlPath.length != 8) {
            return false;
        } else {
            return urlPath[1].chars().allMatch(Character::isDigit) && urlPath[2].equals("seasons") && urlPath[3].chars().allMatch(Character::isDigit) && urlPath[4].equals("days") && urlPath[5].chars().allMatch(Character::isDigit) && urlPath[6].equals("skiers") && urlPath[7].chars().allMatch(Character::isDigit) && Integer.parseInt(urlPath[5]) >= 1 && Integer.parseInt(urlPath[5]) <= 365;
        }
    }
}