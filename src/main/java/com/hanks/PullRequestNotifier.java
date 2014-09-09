package com.hanks;

import com.hanks.Hipchat;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import com.atlassian.event.api.EventListener;
import com.atlassian.stash.event.pull.PullRequestOpenedEvent;
import com.atlassian.stash.event.pull.PullRequestCommentEvent;
import com.atlassian.stash.user.StashUser;
import com.atlassian.stash.pull.PullRequestParticipant;
import com.atlassian.stash.pull.PullRequest;
import com.atlassian.stash.comment.Comment;

public class PullRequestNotifier {
    
    /**
     * Listen to pull request opened event and send the notification
     *
     * @param openedEvent PullRequestOpenedEvent object
     */
	@EventListener
	public void openListener(PullRequestOpenedEvent openedEvent) {
        PullRequest p = openedEvent.getPullRequest();
        
        Set<PullRequestParticipant> reviewers = p.getReviewers();
        String message = getMessageFromOpenedEvent(p);

        if (!reviewers.isEmpty()) {
            for (Iterator it = reviewers.iterator(); it.hasNext();) {   
                try {
                    PullRequestParticipant participant = (PullRequestParticipant)it.next();
                    String reviewer_email = participant.getUser().getEmailAddress();
                    Hipchat.send_message(reviewer_email, message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } 
        }
	}
    
    /**
     * Return message about basic pull request information from open pull request event
     *
     * @param p PullRequest object from PullRequestOpenedEvent
     */
    private String getMessageFromOpenedEvent(PullRequest p) {
        String authorName = p.getAuthor().getUser().getDisplayName();
        String title = p.getTitle();        
        String description = p.getDescription();
        String message = String.format("%s様は最新のPull Requestを作りました。レビューお願いします。\n\nタイトル:\n%s\n\n説明:\n%s\n", authorName, title, description);
        return message;
    }
    
    /**
     * Listen to comment event and send the notification
     *
     * @param commentEvent PullRequestCommentEvent object  
     */
    @EventListener
	public void commentListener(PullRequestCommentEvent commentEvent) {
        PullRequest p = commentEvent.getPullRequest();

        // get all participants related to the pull request, including reviewers, 
        // non-reviewers, pull request author
        Set<PullRequestParticipant> participants = new HashSet<PullRequestParticipant>();
        Set<PullRequestParticipant> reviewers = p.getReviewers();
        Set<PullRequestParticipant> non_reviewers = p.getParticipants();
        PullRequestParticipant pullRequestAuthor = p.getAuthor();

        participants.add(pullRequestAuthor);
        participants.addAll(non_reviewers);
        participants.addAll(reviewers);
        
        // comment author
        StashUser commentAuthor = commentEvent.getComment().getAuthor();
        String commentAuthorEmailAddress = commentAuthor.getEmailAddress();
                
        String message = getMessageFromCommentEvent(commentEvent);
        
        if (!participants.isEmpty()) {
            for (Iterator it = participants.iterator(); it.hasNext();) {
                try {
                    PullRequestParticipant participant = (PullRequestParticipant)it.next();
                    String reviewer_email = participant.getUser().getEmailAddress();
                    // send notification except comment author self.
                    if (reviewer_email != commentAuthorEmailAddress) {
                        Hipchat.send_message(reviewer_email, message);                        
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Return message about basic comment information from comment event
     *
     * @param commentEvent PullRequestCommentEvent object
     */
    private String getMessageFromCommentEvent(PullRequestCommentEvent commentEvent) {
        PullRequest p = commentEvent.getPullRequest();
        String title = p.getTitle();        
        
        Comment comment = commentEvent.getComment();
        String commentAuthorName = comment.getAuthor().getDisplayName();
        String text = comment.getText();
        String message = String.format("%s様がPull Request【%s】をコメントしました。確認お願いします。\n\n詳細:\n%s\n", commentAuthorName, title, text);
        return message;
    }
}
