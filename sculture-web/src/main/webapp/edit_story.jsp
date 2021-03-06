<%@ page import="com.sculture.model.response.StoryResponse" %><%--
  Created by IntelliJ IDEA.
  User: Atakan Arıkan
  Date: 05.01.2016
  Time: 19:19
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath =request.getContextPath();
%>
<html lang="en">

<head>

    <title>Sculture - Add a new story</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="http://fonts.googleapis.com/css?family=Montserrat" rel="stylesheet" type="text/css">
    <link href="http://fonts.googleapis.com/css?family=Lato" rel="stylesheet" type="text/css">
    <link href='https://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="<%out.print(contextPath);%>/public/js/scripts.js"></script>
    <script src="<%out.print(contextPath);%>public/js/bootstrap.min.js"></script>
    <script src="<%out.print(contextPath);%>/public/js/jquery.backstretch.min.js"></script>
    <script src="<%out.print(contextPath);%>/public/js/scripts.js"></script>
    <script src="<%out.print(contextPath);%>/public/js/bootstrap.min.js"></script>
    <script src="<%out.print(contextPath);%>/public/js/jquery.backstretch.min.js"></script>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
    <link rel="stylesheet" href="<%out.print(contextPath);%>/public/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%out.print(contextPath);%>/public/css/font-awesome.css">
    <link rel="stylesheet" href="<%out.print(contextPath);%>/public/css/sweetalert.css">
    <link rel="stylesheet" href="<%out.print(contextPath);%>/public/css/form-elements.css">
    <link rel="stylesheet" href="<%out.print(contextPath);%>/public/css/style.css">
    <link rel="stylesheet" href="<%out.print(contextPath);%>/public/css/homepage_style.css">

</head>

<body id="myPage" data-spy="scroll" data-target=".navbar" data-offset="60">
<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a href="<%out.print(contextPath);%>/index"><img src="<%out.print(contextPath);%>/public/images/logo.png" style="width:204px;height:58px" ;></a>
        </div>
        <div class="collapse navbar-collapse" id="myNavbar">
            <ul class="nav navbar-nav navbar-right">
                <% boolean isLoggedIn = (Boolean) request.getAttribute("isLoggedIn"); %>
                <% if (isLoggedIn) { %>
                <li>
                    <div class="top-big-link">
                        <a class="btn btn-link-2" href="<%out.print(contextPath);%>/addstory">Add Story</a>
                    </div>
                </li>
                <li>
                    <div class="top-big-link">
                        <a class="btn btn-link-2" href="<%out.print(contextPath);%>/logout">Log Out</a>
                    </div>
                </li>
                <li>
                    <%String refUrl = contextPath + "/get/user/" + request.getSession().getAttribute("userid");%>
                    <div class="top-big-link">
                        <a class="btn btn-link-2" href="<%out.print(refUrl);%>" data-modal-id="modal-logout">My
                            Profile</a>
                    </div>
                </li>
                <% } else { %>
                <li>
                    <div class="top-big-link">
                        <a class="btn btn-link-2 launch-modal" href="#" data-modal-id="modal-login">Sign in</a>
                    </div>
                </li>
                <li>
                    <div class="top-link">
                        <a class="btn btn-link-2 launch-modal" href="#" data-modal-id="modal-register">Sign up</a>
                    </div>
                </li>
                <% } %>
            </ul>
        </div>
    </div>
</nav>
<div class="jumbotron text-center">
    <br>
    <% String username = (String)request.getAttribute("username"); %>
    <h1>Sculture!</h1>
    <h3a>Looking good, <%out.print(username);%>!</h3a>
</div>
    <%
                StoryResponse story = (StoryResponse) request.getAttribute("story");

            %>
<div class="container">
    <div class="row">
        <form role="form" method="post" action="<%out.print(contextPath);%>/edit/story/<%out.print(story.getId());%>">
            <div class="col-md-8 col-md-offset-2">
                <div class="form-group">
                    <label for="story-title">Title</label>

                    <div class="input-group">
                        <input type="text" class="form-control" name="story-title" id="story-title" value="<%out.print(story.getTitle());%>">
                        <span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
                    </div>
                </div>
                <div class="form-group">
                    <label for="story-content">Content</label>

                    <div class="input-group">
                                <input type="text" style="padding-bottom: 7px; line-height: 14px; padding-left: 8px; word-break: break-word; padding-right: 8px; height: 230px; font-size: 14px; top: 0px; padding-top: 8px; left: 0px;"
                                   name="story-content" id="story-content" class="form-control" value="<%out.print(story.getContent());%>">
                        <span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
                    </div>
                </div>
                <%String tagsSeparated = "";
                    for(String tag : story.getTags()){
                        tagsSeparated += tag + " ";
                    }
                    tagsSeparated = tagsSeparated.trim();%>
                <div class="input-group">
                    <input type="text" class="form-control" id="story-tags" name="story-tags" value="<%out.print(tagsSeparated);%>">
                    <span class="input-group-addon"></span>
                </div>
                <%String mediaSeparated = "";
                    for(String media : story.getMedia()){
                        mediaSeparated += media + " ";
                    }
                    mediaSeparated = mediaSeparated.trim();%>

                <input type="hidden" class="form-control" id="story-media" name="story-media" value="<%out.print(mediaSeparated);%>">
                <br>
                <div container>
                    <input type="submit" name="submit" id="submit" value="Edit Story" class="btn btn-info pull-right">
                </div>

            </div>
        </form>
    </div>
</div>
<br>
<br><br>
<!-- LOGIN -->
<div class="modal fade" id="modal-login" tabindex="-1" role="dialog" aria-labelledby="modal-login-label" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">

            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                </button>
                <h3 class="modal-title" id="modal-login-label">Sign in to Sculture</h3>
                <p>Enter your username and password to sign in:</p>
            </div>

            <div class="modal-body">

                <form role="form" action="<%out.print(contextPath);%>/login" method="post" class="login-form">
                    <div class="form-group">
                        <label class="sr-only" for="form-email">E-mail</label>
                        <input type="text" name="form-email" placeholder="Email..." class="form-email form-control" id="form-email">
                    </div>
                    <div class="form-group">
                        <label class="sr-only" for="form-password">Password</label>
                        <input type="password" name="form-password" placeholder="Password..." class="form-password form-control" id="form-password">
                    </div>
                    <button type="submit" class="btn">Sign in!</button>
                    <div style="text-align: center;">
                        <br> Or sign in using: <br>
                        <a class="btn btn-link-1" href="#">
                            <i class="fa fa-facebook"></i> Facebook
                        </a>
                    </div>
                </form>

            </div>

        </div>
    </div>
</div>

<!-- REGISTER -->
<div class="modal fade" id="modal-register" tabindex="-1" role="dialog" aria-labelledby="modal-register-label" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">

            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                </button>
                <h3 class="modal-title" id="modal-register-label">Register to Sculture</h3>
                <p>Fill out the fields below:</p>
            </div>

            <div class="modal-body">

                <form role="form" action="<%out.print(contextPath);%>/signup" method="post" class="register-form">
                    <div class="form-group">
                        <label class="sr-only" for="form-email">E-mail</label>
                        <input type="text" name="form-email" placeholder="Enter your email" class="form-email form-control" id="form-email">
                    </div>
                    <div class="form-group">
                        <label class="sr-only" for="form-username">Username</label>
                        <input type="text" name="form-username" placeholder="Enter your username" class="form-bane form-control" id="form-username">
                    </div>
                    <div class="form-group">
                        <label class="sr-only" for="form-password">Password</label>
                        <input type="password" name="form-password" placeholder="Enter your password" class="form-password form-control" id="form-password">
                    </div>
                    <div class="form-group">
                        <label class="sr-only" for="form-retypedpassword">Password</label>
                        <input type="password" name="form-retypedpassword" placeholder="Retype your password..." class="form-retypedpassword form-control" id="form-retypedpassword">
                    </div>
                    <button type="submit" class="btn">Sign up!</button>
                    <div style="text-align: center;">
                        <br> Or sign up using: <br>
                        <a class="btn btn-link-1" href="#">
                            <span><i class="fa fa-facebook"></i> Facebook</span>
                        </a>
                    </div>
                </form>

            </div>

        </div>
    </div>
</div>
<script>
    $(document).ready(function(){
        // Add smooth scrolling to all links in navbar + footer link
        $(".navbar a, footer a[href='#myPage']").on('click', function(event) {

            // Prevent default anchor click behavior
            //   event.preventDefault();

            // Store hash
            var hash = this.hash;

            // Using jQuery's animate() method to add smooth page scroll
            // The optional number (900) specifies the number of milliseconds it takes to scroll to the specified area
            $('html, body').animate({
                scrollTop: $(hash).offset().top
            }, 900, function(){

                // Add hash (#) to URL when done scrolling (default click behavior)
                window.location.hash = hash;
            });
        });

        // Slide in elements on scroll
        $(window).scroll(function() {
            $(".slideanim").each(function(){
                var pos = $(this).offset().top;

                var winTop = $(window).scrollTop();
                if (pos < winTop + 600) {
                    $(this).addClass("slide");
                }
            });
        });
    })
</script>
<div id="contact" class="container-fluid bg-grey">
    <p><span class="glyphicon glyphicon-map-marker"></span> Istanbul, TR</p>

    <p><span class="glyphicon glyphicon-phone"></span> +90 212 359 54 00</p>

    <p><span class="glyphicon glyphicon-envelope"></span> info@sculture.com</p>
</div>

