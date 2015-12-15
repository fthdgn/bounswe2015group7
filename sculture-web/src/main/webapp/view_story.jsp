<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="com.sculture.model.response.StoryResponse" %>
<%@ page import="com.sculture.model.response.CommentResponse" %>
<!DOCTYPE html>


<html lang="en">

<head>

    <title>Sculture - share your culture!</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="http://fonts.googleapis.com/css?family=Montserrat" rel="stylesheet" type="text/css">
    <link href="http://fonts.googleapis.com/css?family=Lato" rel="stylesheet" type="text/css">
    <link href='https://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="/public/js/scripts.js"></script>
    <script src="/public/js/bootstrap.min.js"></script>
    <script src="/public/js/jquery.backstretch.min.js"></script>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
    <link rel="stylesheet" href="/public/css/bootstrap.min.css">
    <link rel="stylesheet" href="/public/css/font-awesome.css">
    <link rel="stylesheet" href="/public/css/sweetalert.css">
    <link rel="stylesheet" href="/public/css/form-elements.css">
    <link rel="stylesheet" href="/public/css/style.css">
    <link rel="stylesheet" href="/public/css/homepage_style.css">
    <link rel="stylesheet" href="/public/css/storystyle.css">


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
            <a href="/index"><img src="/public/images/logo.png" style="width:204px;height:58px" ;></a>
        </div>
        <div class="collapse navbar-collapse" id="myNavbar">
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <div class="top-big-link">
                        <a class="btn btn-link-2" href="/addstory" data-modal-id="modal-create-story">Add Story</a>
                    </div>
                </li>
                <% Boolean isLoggedIn = (Boolean) request.getAttribute("isLoggedIn"); %>
                <% if (isLoggedIn.booleanValue()) { %>
                <li>
                    <div class="top-big-link">
                        <a class="btn btn-link-2" href="/logout" data-modal-id="modal-logout">Log Out</a>
                    </div>
                </li>
                <li>
                    <%String refUrl = "/get/user/" + request.getSession().getAttribute("userid");%>
                    <div class="top-big-link">
                        <a class="btn btn-link-2" href="<%out.print(refUrl);%>" data-modal-id="modal-logout">My Profile</a>
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
    <% String username = (String) request.getAttribute("username"); %>
    <h1>Sculture!</h1>
    <h3a>Looking good, <%out.print(username);%>!</h3a>
    <form class="form-inline" action="/search" method="post">
        <br> <br>
        <input type="text" name="main-search" id="main-search" class="form-control" size="50"
               placeholder="Search stories" required>
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
                StoryResponse story = (StoryResponse) request.getAttribute("story");

            %>

            <h1><% out.print(story.getTitle()); %></h1>

            <hr>
            <!-- Preview Image -->
            <% try { %>
            <%if (story.getMedia() != null) { %>
            <img  src="<%out.print("http://52.28.216.93:9000/image/get/" + story.getMedia().get(0));%>" alt="">
            <% } %>
            <%} catch (Exception e) {%>
            <img  style="width: 250px; height: 300px" src="http://en.mladinsko.com/images/emptyMME.gif" alt="">
            <% }%>
            <hr>
            <p><% out.print(story.getContent().replace("\n","<br>")); %></p>



            <hr>

            <!-- Date/Time -->

            <%
                try{
                    Date storyCreationDate = story.getCreation_date();%>
            <p><span class="glyphicon glyphicon-time"></span> Posted on: <% out.print(storyCreationDate); %></p>

            <%} catch (Exception e) { %>
            <p><span class="glyphicon glyphicon-time"></span> Posted on: </p>

            <%} %>
            <hr>

            <!-- Post Content -->
                <span class="center-block">
                    <i id="like1" class="glyphicon glyphicon-thumbs-up"></i> <span id="like1-bs3"> <%out.print(story.getPositive_vote());%></span>
                    <i id="dislike1" class="glyphicon glyphicon-thumbs-down"></i> <span id="dislike1-bs3"><%out.print(story.getNegative_vote());%></span>
                </span>

            <hr>

            <!-- Blog Comments -->
            <!-- Comments Form -->
            <div class="well">
                <h4>Leave a Comment:</h4>

                <form action="/addcomment" method="POST" role="form">
                    <div class="form-group">
                        <input type="text" name="form-commentbody" id="form-commentbody" class="form-control" rows="3"></textarea>
                    </div>
                        <input type="hidden" name="story_id" id="story_id" value="<%out.print(story.getId());%>" class="form-control"></textarea>
                    <button type="submit" class="btn btn-primary">Submit</button>
                </form>
            </div>

            <hr>

            <!-- Posted Comments -->

            <!-- Comment -->
            <%ArrayList<CommentResponse> comments = (ArrayList<CommentResponse>)request.getAttribute("comments");%>

            <%for(int i = 0; i < comments.size(); i++) {%>
            <div class="media">
                <a class="pull-left" href="#">
                    <img class="media-object" height="64" width="64" src="https://cdn3.iconfinder.com/data/icons/line-icons-large-version/64/comment-512.png" alt="">
                </a>
                <div class="media-body">
                    <h4 class="media-heading" align="left"> <%out.print(comments.get(i).getOwner_username());%>
                        <%Timestamp stamp = new Timestamp(Long.parseLong(comments.get(i).getCreate_date()));
                            Date commentCreationDate = new Date(stamp.getTime());%>
                        <small><%out.print(commentCreationDate);%>
                    </h4>
                    <%out.print(comments.get(i).getContent());%>
                </div>

            </div>
            <%}%>


        </div>

        <!-- Blog Sidebar Widgets Column -->
        <div class="col-md-4">

            <div class="well">
                <h4>Created by:</h4>
                <%String refUrl = "/get/user/" + story.getOwnerId();%>
                <a href="<%out.print(refUrl);%>" type="button" class="btn btn-link-1" style="height:50px;width:300px"> <%out.print(story.getOwner());%> </a>

            </div>

            <!-- Tags -->
            <div class="well">
                <h4>Story Tags</h4>

                <div class="row">
                    <div class="col-lg">
                        <ul class="list-unstyled">
                            <%if(story.getTags() != null)  { %>
                            <% for (int i = 0; i < story.getTags().size(); i++) { %>
                            <li><a href="#"><% out.print(story.getTags().get(i)); %> </a>
                            </li>
                            <%}
                                }%>
                        </ul>
                    </div>
                </div>
                <!-- /.row -->
            </div>
            <div class="well">
                <%String asd = "/report/story/" + story.getStory_id();%>
                <a href="<%out.print(asd);%>" type="button" class="btn btn-link-1" style="height:50px;width:300px"> Report Story </a>

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
<div class="modal fade" id="modal-login" tabindex="-1" role="dialog" aria-labelledby="modal-login-label"
     aria-hidden="true">
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
                        <input type="text" name="form-username" placeholder="Username..."
                               class="form-email form-control" id="form-username">
                    </div>
                    <div class="form-group">
                        <label class="sr-only" for="form-password">Password</label>
                        <input type="password" name="form-password" placeholder="Password..."
                               class="form-password form-control" id="form-password">
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
<div class="modal fade" id="modal-register" tabindex="-1" role="dialog" aria-labelledby="modal-register-label"
     aria-hidden="true">
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
                        <input type="text" name="form-email" placeholder="Enter your email"
                               class="form-email form-control" id="form-email">
                    </div>
                    <div class="form-group">
                        <label class="sr-only" for="form-username">Username</label>
                        <input type="text" name="form-username" placeholder="Enter your username"
                               class="form-bane form-control" id="form-username">
                    </div>
                    <div class="form-group">
                        <label class="sr-only" for="form-password">Password</label>
                        <input type="password" name="form-password" placeholder="Enter your password"
                               class="form-password form-control" id="form-password">
                    </div>
                    <div class="form-group">
                        <label class="sr-only" for="form-retypedpassword">Password</label>
                        <input type="password" name="form-retypedpassword" placeholder="Retype your password..."
                               class="form-retypedpassword form-control" id="form-retypedpassword">
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
            <input type="hidden" id="definitelynottheaccesstoken" value="<%out.print(request.getSession().getAttribute("access-token"));%>">
        </div>
    </div>
</div>
<!-- Javascript -->
<script src="public/js/jquery-1.11.1.min.js"></script>
<script src="public/js/bootstrap.min.js"></script>
<script src="public/js/jquery.backstretch.min.js"></script>
<script src="public/js/scripts.js"></script>
<script>
    $(document).ready(function () {
        // Add smooth scrolling to all links in navbar + footer link
        $(".navbar a, footer a[href='#myPage']").on('click', function (event) {

            // Prevent default anchor click behavior
            //   event.preventDefault();

            // Store hash
            var hash = this.hash;

            // Using jQuery's animate() method to add smooth page scroll
            // The optional number (900) specifies the number of milliseconds it takes to scroll to the specified area
            $('html, body').animate({
                scrollTop: $(hash).offset().top
            }, 900, function () {

                // Add hash (#) to URL when done scrolling (default click behavior)
                window.location.hash = hash;
            });
        });

        // Slide in elements on scroll
        $(window).scroll(function () {
            $(".slideanim").each(function () {
                var pos = $(this).offset().top;

                var winTop = $(window).scrollTop();
                if (pos < winTop + 600) {
                    $(this).addClass("slide");
                }
            });
        });
        $('.glyphicon-thumbs-up, .glyphicon-thumbs-down').click(function(){
            var $this = $(this);
            var story_id = <%out.print(story.getId());%>;
            var definitelynottheaccesstoken = <%out.print(request.getSession().getAttribute("access_token"));%>;
            var vote;
            if(this.id == "like1") vote = 1;
            else if(this.id == "dislike1") vote = -1;
            if(definitelynottheaccesstoken == null) vote = 0;
            $.ajax({
                type: 'POST',
                beforeSend: function (request)                {
                    request.setRequestHeader("access-token", definitelynottheaccesstoken);
                },
                url: 'http://52.28.216.93:9000/story/vote',
                contentType: "application/json",
                data: JSON.stringify({
                    "story_id": story_id,
                    "vote" : vote
                }),
                success: function (myData) {
                    location.reload();
                },
                error:function (errorData) {
                    alert("error!");
                }
            });
        });
    })
</script>
</body>

</html>