package com.niezhiliang.netty.websocket.spring.boot.demo.service;

import com.niezhiliang.netty.websocket.spring.boot.demo.controller.TestController;
import com.niezhiliang.netty.websocket.starter.annotations.*;
import com.niezhiliang.netty.websocket.starter.socket.Session;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * @author nzl
 * @date 2023/6/21
 */
@WsServerEndpoint(value = "/websocket/{uid}/{arg}")
public class ServerEndpoint {
@Autowired
    TestController testController;

    @HandshakeBefore
    public void before (HttpHeaders headers) {
        System.out.println("before");
        testController.test();
    }

    /**
     * 用户连接时触发
     * @param session
     */
    @OnOpen
    public void open(Session session, @PathParam (value="uid") String uid, @PathParam String arg){
        System.out.println("open");
        session.sendText("hello client");
    }

    /**
     * 收到信息时触发
     * @param message
     */
    @OnMessage
    public void onMessage(Session session,String message){
        System.out.println("message:" + message);
        session.sendText("server: " + message);
    }

    /**
     * 连接关闭触发
     */
    @OnClose
    public void onClose(){
        System.out.println("close  " + LocalDateTime.now());
    }

    /**
     * 发生错误时触发
     * @param session
     * @param e
     */
    @OnError
    public void onError(Session session, Throwable e) {
        System.out.println("onError");
    }

    /**
     * 发生事件时触发
     * @param session
     * @param evt
     */
    @OnEvent
    public void onEvent(Session session, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    System.out.println("read idle");
                    break;
                case WRITER_IDLE:
                    System.out.println("write idle");
                    session.close();
                    break;
                case ALL_IDLE:
                    System.out.println("all idle");
                    break;
                default:
                    break;
            }
        }
    }
}
