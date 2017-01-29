package de.mrfrey.photos.store.web;

import de.mrfrey.photos.store.PhotoComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class CommentController {
    private static Logger logger = LoggerFactory.getLogger(CommentController.class);

    @MessageMapping("/comments")
    public PhotoComment photoComment(@Payload PhotoComment comment, Principal user) {
        comment.setUser(user.getName());
        logger.info(comment.toString());
        return comment;
    }
}
