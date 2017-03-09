package de.mrfrey.photos.store.web;

import de.mrfrey.photos.store.photo.PhotoStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CommentController {
    private static Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final PhotoStorageService photoStorageService;

    public CommentController(PhotoStorageService photoStorageService) {
        this.photoStorageService = photoStorageService;
    }

    @MessageMapping("/comments")
    public PhotoComment photoComment(@Payload PhotoComment comment, Principal user) {
        String userName = user.getName();
        comment.setUser(userName);
        logger.info(comment.toString());
        photoStorageService.addComment(comment.getPhotoId(), userName, comment.getComment());
        return comment;
    }

    @MessageExceptionHandler
    @SendToUser("/exchange/amq.direct/errors")
    public Map<String, Object> handleException(StompHeaderAccessor headers, Exception x) {
        Map<String, Object> error = new HashMap<>();
        error.put("command", headers.getCommand().getMessageType());
        error.put("message-id", headers.getMessageId());
        error.put("destination", headers.getDestination());
        error.put("content-type", headers.getContentType());
        error.put("exception", x.getClass().getName());
        error.put("message", x.getMessage());
        return error;
    }
}
