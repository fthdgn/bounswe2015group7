<!DOCTYPE html>
 <%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*" %>

<html lang="en">

<head>

    <title>Sculture - share your culture!</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="http://fonts.googleapis.com/css?family=Montserrat" rel="stylesheet" type="text/css">
    <link href="http://fonts.googleapis.com/css?family=Lato" rel="stylesheet" type="text/css">
    <link href='https://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="public/js/sweetalert.min.js"></script>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
    <link rel="stylesheet" href="public/css/bootstrap.min.css">
    <link rel="stylesheet" href="public/css/font-awesome.css">
    <link rel="stylesheet" href="public/css/form-elements.css">
    <link rel="stylesheet" href="public/css/sweetalert.css">
    <link rel="stylesheet" href="public/css/style.css">
    <link rel="stylesheet" href="public/css/homepage_style.css">
    <link rel="stylesheet" href="public/css/storystyle.css">


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
            <a href="/"><img src="public/images/logo.png" style="width:204px;height:58px";></a>
        </div>
        <div class="collapse navbar-collapse" id="myNavbar">
            <ul class="nav navbar-nav navbar-right">
               <li>
                   <div class="top-big-link">
                       <a class="btn btn-link-2" href="/addstory" data-modal-id="modal-create-story">Add Story</a>
                   </div>
               </li>
               <% boolean isLoggedIn = request.getAttribute(isLoggedIn); %>
                  <% if (isLoggedIn) { %>
               <li>
                   <div class="top-big-link">
                       <a class="btn btn-link-2" href="/logout" data-modal-id="modal-logout">Log Out</a>
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
    <% String username = request.getAttribute("username"); %>
    <h1>Sculture!</h1>
    <h3a>Looking good, <%out.print(username);%>!</h3a>
    <form class="form-inline" action="/search" method="post">
        <br> <br>
        <input type="text" name="main-search" id="main-search" class="form-control" size="50" placeholder="Search stories" required>
    </form>
    <br>
    <a class="btn btn-link-2" href="/search/all" data-modal-id="modal-create-story">All stories</a>
    <br>
</div>


<div class="container">

    <div class="row">

        <!-- Blog Post Content Column -->
        <div class="col-lg-8">

            <!-- Blog Post -->

            <!-- Title -->
            <%
            String title = request.getAttribute("storyTitle");
            String date = request.getAttribute("storyCreationDate");
            String content = request.getAttribute("storyContent");
            String[] tags = request.getAttribute("storyTags");

            %>

            <h1><% out.print(title); %></h1>

            <hr>

            <!-- Date/Time -->
            <p><span class="glyphicon glyphicon-time"></span> Posted on: <% out.print(date); %></p>

            <hr>

            <!-- Preview Image -->
            <!--      <img class="img-responsive" src="http://static.independent.co.uk/s3fs-public/styles/story_large/public/thumbnails/image/2014/01/16/18/v236-animal-fights-ala.jpg" alt="">

                  <hr>

                  <!-- Post Content -->
            <p><% out.print(content); %>
            <hr>

            <!-- Blog Comments -->

            <!-- Comments Form -->
            <div class="well">
                <h4>Leave a Comment:</h4>
                <form role="form">
                    <div class="form-group">
                        <textarea class="form-control" rows="3"></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary">Submit</button>
                </form>
            </div>

            <hr>

            <!-- Posted Comments -->

            <!-- Comment -->
            <div class="media">
                <a class="pull-left" href="#">
                    <img class="media-object" src="http://placehold.it/64x64" alt="">
                </a>
                <div class="media-body">
                    <h4 class="media-heading" align="left">   Josh Adams
                        <small>November 9, 2015 at 9:30 PM</small>
                    </h4>
                    Amazing pictures! I was also at the Selcuk Efes Camel Wrestling Festival last year, it was such a fascinating experience. I heard that this tradition was popular in my hometown in Texas during the 1850s! How crazy is that?
                </div>
            </div>

            <!-- Comment -->
            <div class="media">
                <a class="pull-left" href="#">
                    <img class="media-object" src="http://placehold.it/64x64" alt="">
                </a>
                <div class="media-body">
                    <h4 class="media-heading" align="left"> Justin Graham
                        <small>August 25, 2014 at 9:30 PM</small>
                    </h4>
                    I’ve been to a camel market in Egypt, but I didn't know they also had camel wrestling! Does the festival go on throughout the year? I would love to see it!
                    <!-- Nested Comment -->
                    <div class="media">
                        <a class="pull-left" href="#">
                            <img class="media-object" src="http://placehold.it/64x64" alt="">
                        </a>
                        <div class="media-body">
                            <h4 class="media-heading" align="left"> Narin Mahmuti
                                <small>August 25, 2014 at 9:30 PM</small>
                            </h4>
                            From what I have heard they have camel wrestling in other countries so maybe in Egypt as well. Sadly, the festival goes on only for three months of the year, while they are on heat.
                        </div>
                    </div>
                    <!-- End Nested Comment -->
                </div>
            </div>

        </div>

        <!-- Blog Sidebar Widgets Column -->
        <div class="col-md-4">

            <!-- Blog Search Well -->
            <div class="well">
                <h4>Search for a story:</h4>
                    <form role="form" action="/search" method="post">
                        <div class="form-group has-feedback">
                            <label class="control-label" for="main-search">
                            </label>
                            <input type="text" class="form-control" name="main-search" id="main-search"
                                   placeholder="Tag name" /> <span
                                class="glyphicon glyphicon-search form-control-feedback"></span>
                        </div>
                    </form>

                <!-- /.input-group -->
            </div>

            <!-- Tags -->
            <div class="well">
                <h4>Story Tags</h4>
                <div class="row">
                    <div class="col-lg">
                        <ul class="list-unstyled">
                            <% for (int i = 0; i < tags.length; i++) { %>
                            <li><a href=""><% out.print(tags[i])%></a>
                            </li>
                            <% } %>
                        </ul>
                    </div>
                </div>
                <!-- /.row -->
            </div>


        </div>

    </div>
    <!-- /.row -->

    <hr>

</div>










<div id="contact" class="container-fluid bg-grey">
    <p><span class="glyphicon glyphicon-map-marker"></span> Istanbul, TR</p>
    <p><span class="glyphicon glyphicon-phone"></span> +90 212 359 54 00</p>
    <p><span class="glyphicon glyphicon-envelope"></span> info@sculture.com</p>
</div>

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

                <form role="form" action="/login" method="post" class="login-form">
                    <div class="form-group">
                        <label class="sr-only" for="form-username">E-mail</label>
                        <input type="text" name="form-username" placeholder="Username..." class="form-email form-control" id="form-username">
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

                <form role="form" action="/signup" method="post" class="register-form">
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
<!-- Javascript -->
<script src="public/js/jquery-1.11.1.min.js"></script>
<script src="public/js/bootstrap.min.js"></script>
<script src="public/js/jquery.backstretch.min.js"></script>
<script src="public/js/scripts.js"></script>
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
</body>

</html>