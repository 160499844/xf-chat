package com.xiaofeng.netty.server.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xiaofeng.global.UserInfoContext;
import com.xiaofeng.utils.user.UserToken;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;

/**
 * 处理http请求
 * 
 * @author xiaofeng
 *
 */
public class HttpHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String userId = ctx.channel().id().asLongText();
		if (msg instanceof FullHttpRequest) {

			FullHttpRequest req = (FullHttpRequest) msg;

			try {

				// 1.获取URI
				String uri = req.uri();

				// 2.获取请求体
				ByteBuf buf = req.content();
				String content = buf.toString(CharsetUtil.UTF_8);

				// 3.获取请求方法
				HttpMethod method = req.method();

				// 4.获取请求头
				HttpHeaders headers = req.headers();
				Map<String, String> parmMap = new HashMap<>();
				// 5.根据method，确定不同的逻辑
				if (method.equals(HttpMethod.GET)) {
					QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
					decoder.parameters().entrySet().forEach(entry -> {
						// entry.getValue()是一个List, 只取第一个元素
						parmMap.put(entry.getKey(), entry.getValue().get(0));
					});
				}
				if (method.equals(HttpMethod.POST)) {
					// 接收用户输入，并将输入返回给用户
					// 是POST请求
					HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(req);
					decoder.offer(req);

					List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();

					for (InterfaceHttpData parm : parmList) {

						Attribute data = (Attribute) parm;
						parmMap.put(data.getName(), data.getValue());
					}
				}
				//ChannelHandlerContext channelHandlerContext = UserInfoContext.USER_SESSION.get(userId);
				
				//增加用户信息
//				UserToken u = new UserToken();
//				u.setUserId(userId);
//				u.setUserName(parmMap.get("userName"));
//				UserInfoContext.addUser(userId, u);
				response(ctx, "成功");
			} finally {
				req.release();
			}
		}
	}

	private void response(ChannelHandlerContext ctx, String msg) {

		// 1.设置响应
		FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
				Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));

		resp.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");

		// 2.发送
		// 注意必须在使用完之后，close channel
		ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
	}

}
