
Pull Request Notifier Plugin For Stash
=======================================

A notification plugin for Stash using hipchat to notify reviewers when pull requests are created, or all the participants when comments are replied.


## Version History

###_VERSION_ 1.0 - 2014/09/12
+ First release upon the world  

## Why
Pull request is often used as a way of code review during the teamwork, but sometimes we create a pull request and the reviewer is too late to do the review, or someone creates a comment, we notice it very lately.   

In another word, the pull request is good, but the communication is not efficient during the work. So I create this plugin to use hipchat to do the **real time** notification, and also pick up some outline information as the message to let us catch up the idea more quickly.

## Demo
![alt text][demo]

[demo]: https://raw.githubusercontent.com/hanks/Pull_request_notifier_for_Stash/master/demo/demo.gif "demo"

## Usage
1. Update the V2 API auth token of your notification account in the Constants.java
2. Install this plugin to your own Stash Server

## Prerequisites
1. A valid hipchat account to do notification
2. SDK for Stash plugin development, it needs a **Java** environment, you can set it up follow the tutorial <a href="https://developer.atlassian.com/display/DOCS/Set+up+the+Atlassian+Plugin+SDK+and+Build+a+Project">here</a>.

## Implementation  
Actually, I finish this plugin by three steps.
  
1. Implement sending message by hipchat, here I use <a href="https://www.hipchat.com/docs/apiv2">V2 API</a> of hipchat, and send post request by Java code.   
2. Listen <a href="https://developer.atlassian.com/static/javadoc/stash/3.2.4/api/reference/com/atlassian/stash/event/pull/PullRequestOpenedEvent.html">Pull Request Opened Event</a> to do notifications to the reviewers.
3. Listen <a href="https://developer.atlassian.com/static/javadoc/stash/3.2.4/api/reference/com/atlassian/stash/event/pull/PullRequestCommentEvent.html">Comment Event</a> to do notification to all the participants of one pull request.  
  
<a href="https://developer.atlassian.com/stash/docs/3.0.1/how-tos/responding-to-application-events.html">Here</a> is materials about how to hook event in the Stash framework.

## Contribution
**Waiting for your pull requests**

## Lisence
MIT Lisence
