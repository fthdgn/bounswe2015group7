package sculture.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import sculture.Utils;
import sculture.dao.*;
import sculture.exceptions.*;
import sculture.lucene.SearchEngine;
import sculture.models.requests.*;
import sculture.models.response.*;
import sculture.models.tables.Comment;
import sculture.models.tables.Story;
import sculture.models.tables.Tag;
import sculture.models.tables.User;
import sculture.models.tables.relations.TagStory;

import java.io.FileOutputStream;
import java.util.*;

import static sculture.Utils.*;

@RestController
public class SCultureRest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private VoteStoryDao voteStoryDao;

    @Autowired
    private StoryDao storyDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private TagStoryDao tagStoryDao;

    @Autowired
    private FollowUserDao followUserDao;

    @Autowired
    private ReportStoryDao reportStoryDao;

    @RequestMapping(method = RequestMethod.POST, value = "/user/register")
    public LoginResponse user_register(@RequestBody RegisterRequestBody requestBody) {
        String email = requestBody.getEmail();
        String username = requestBody.getUsername();
        String password = requestBody.getPassword();
        String fullname = requestBody.getFullname();
        long facebook_id = requestBody.getFacebook_id();
        String facebook_token = requestBody.getFacebook_token();

        if (!checkEmailSyntax(email))
            throw new InvalidEmailException();

        if (!checkUsernameSyntax(username))
            throw new InvalidUsernameException();

        if (!checkPasswordSyntax(password))
            throw new InvalidPasswordException();


        User u = new User();
        u.setEmail(email);
        u.setUsername(username);
        u.setPassword_hash(Utils.password_hash(password));
        if (requestBody.getFullname() != null) u.setFullname(fullname);
        if (requestBody.getFacebook_id() != 0) u.setFacebook_id(facebook_id);
        if (requestBody.getFacebook_token() != null) u.setFacebook_token(facebook_token);
        u.setAccess_token(Utils.access_token_generate());

        try {
            userDao.create(u);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException();
        }
        return new LoginResponse(u);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/update")
    public LoginResponse user_update(@RequestBody UserUpdateRequestBody requestBody, @RequestHeader HttpHeaders headers) {
        User u = getCurrentUser(headers, true);

        String email = requestBody.getEmail();
        String username = requestBody.getUsername();
        String old_password = requestBody.getOld_password();
        String new_password = requestBody.getNew_password();
        String fullname = requestBody.getFullname();

        if (!checkEmailSyntax(email))
            throw new InvalidEmailException();

        if (!checkUsernameSyntax(username))
            throw new InvalidUsernameException();

        if (!checkPasswordSyntax(old_password))
            throw new InvalidPasswordException();

        if (!checkPasswordSyntax(new_password))
            throw new InvalidPasswordException();

        if (u.getPassword_hash().equals(Utils.password_hash(old_password))) {
            u.setEmail(email);
            u.setUsername(username);
            u.setPassword_hash(Utils.password_hash(new_password));

            if (fullname != null) u.setFullname(fullname);
            userDao.update(u);
            return new LoginResponse(u);
        } else
            throw new WrongPasswordException();

    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/get")
    public UserGetResponse user_get(@RequestBody UserGetRequestBody requestBody, @RequestHeader HttpHeaders headers) {
        User current_user = getCurrentUser(headers, false);

        long id = requestBody.getUserId();
        User u;
        try {
            u = userDao.getById(id);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            throw new UserNotExistException();
        }

        UserGetResponse response = new UserGetResponse();
        response.setEmail(u.getEmail());
        response.setUser_id(u.getUser_id());
        response.setUsername(u.getUsername());
        if (current_user != null)
            response.setIs_following(followUserDao.get(current_user.getUser_id(), u.getUser_id()).is_follow());
        else
            response.setIs_following(false);
        return response;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/search/all")
    public SearchResponse user_get(@RequestBody SearchAllRequestBody requestBody, @RequestHeader HttpHeaders headers) {
        User current_user = getCurrentUser(headers, false);

        int page = requestBody.getPage();
        int size = requestBody.getSize();
        if (size < 1)
            size = 10;
        if (page < 1)
            page = 1;
        SearchResponse response = new SearchResponse();
        List<StoryResponse> storyResponses = new ArrayList<>();
        response.setResult(storyResponses);
        List<Story> stories = storyDao.getAll(page, size);

        for (Story story : stories) {
            storyResponses.add(new StoryResponse(story, current_user, tagStoryDao, userDao, voteStoryDao));
        }

        response.setResult(storyResponses);
        return response;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tag/get")
    public TagResponse user_get(@RequestBody TagGetRequestBody requestBody) {
        String title = requestBody.getTag_title();
        Tag tag;
        try {
            tag = tagDao.getByTitle(title);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            throw new UserNotExistException();
        }
        return new TagResponse(tag, userDao.getById(tag.getLast_editor_id()).getUsername());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tag/edit")
    public TagResponse tag_edit(@RequestBody TagEditRequestBody requestBody, @RequestHeader HttpHeaders headers) {
        User current_user = getCurrentUser(headers, true);
        Tag tag = new Tag();
        tag.setTag_title(requestBody.getTag_title());
        tag.setIs_location(false);
        tag.setTag_description(requestBody.getTag_description());
        tag.setLast_editor_id(current_user.getUser_id());
        tag.setLast_edit_date(new Date());
        tagDao.update(tag);
        return new TagResponse(tag, userDao.getById(tag.getLast_editor_id()).getUsername());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/stories")
    public SearchResponse user_get(@RequestBody StoriesGetRequestBody requestBody, @RequestHeader HttpHeaders headers) {
        User current_user = getCurrentUser(headers, false);
        //TODO Exception handling
        int page = requestBody.getPage();
        int size = requestBody.getSize();
        if (size < 1)
            size = 10;
        if (page < 1)
            page = 1;

        long id = requestBody.getId();

        List<Story> storyList = storyDao.getByOwner(id, page, size);

        SearchResponse searchResponse = new SearchResponse();
        List<StoryResponse> responses = new ArrayList<>();
        for (Story story : storyList) {
            responses.add(new StoryResponse(story, current_user, tagStoryDao, userDao, voteStoryDao));
        }
        searchResponse.setResult(responses);
        return searchResponse;
    }

    final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";

    final java.util.Random rand = new java.util.Random();

    // consider using a Map<String,Boolean> to say whether the identifier is being used or not
    final Set<String> identifiers = new HashSet<String>();

    public String randomIdentifier() {
        StringBuilder builder = new StringBuilder();
        while (builder.toString().length() == 0) {
            int length = rand.nextInt(5) + 5;
            for (int i = 0; i < length; i++)
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            if (identifiers.contains(builder.toString()))
                builder = new StringBuilder();
        }
        return builder.toString();
    }

    @RequestMapping(value = "/image/upload", method = RequestMethod.POST)
    public
    @ResponseBody
    ImageResponse handleFileUpload(
            @RequestBody byte[] file) throws Exception {
        String randomIdentifier = randomIdentifier();
        FileOutputStream fos = new FileOutputStream("/image/" + randomIdentifier + ".jpg");
        fos.write(file);
        fos.close();
        ImageResponse imageResponse = new ImageResponse();
        imageResponse.setId(randomIdentifier);
        return imageResponse;
    }

    private FileSystemResourceLoader resourceLoader = new FileSystemResourceLoader();

    @RequestMapping(method = RequestMethod.GET, value = "/image/get/{id}", produces = "image/jpg")
    public Resource image_get(@PathVariable String id) {
        return resourceLoader.getResource("file:/image/" + id + ".jpg");
    }


    @RequestMapping(method = RequestMethod.POST, value = "/user/login")
    public LoginResponse user_login(@RequestBody LoginRequestBody requestBody) {
        String email = requestBody.getEmail();
        String password = requestBody.getPassword();
        String username = requestBody.getUsername();

        if (!checkEmailSyntax(email) && username == null)
            throw new InvalidEmailException();


        if (!checkPasswordSyntax(password))
            throw new InvalidPasswordException();


        User u;
        try {
            if (email != null) {
                u = userDao.getByEmail(email);
            } else u = userDao.getByUsername(username);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            throw new UserNotExistException();
        }


        if (u.getPassword_hash().equals(Utils.password_hash(password))) {
            return new LoginResponse(u);

        } else
            throw new WrongPasswordException();
    }


    @RequestMapping(method = RequestMethod.POST, value = "/user/follow")
    public UserFollowResponse user_follow(@RequestBody UserFollowRequestBody requestBody, @RequestHeader HttpHeaders headers) {
        User current_user = getCurrentUser(headers, true);
        followUserDao.follow_user(current_user.getUser_id(), requestBody.getUser_id(), requestBody.is_follow());
        return new UserFollowResponse(requestBody.is_follow());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/story/create")
    public StoryResponse story_create(@RequestBody StoryCreateRequestBody requestBody, @RequestHeader HttpHeaders headers) {
        User current_user = getCurrentUser(headers, true);

        //TODO Exception handling

        Date date = new Date();
        Story story = new Story();

        story.setTitle(requestBody.getTitle());
        story.setContent(requestBody.getContent());
        story.setOwner_id(current_user.getUser_id());
        story.setCreate_date(date);
        story.setLast_edit_date(date);
        story.setLast_editor_id(current_user.getUser_id());
        if (requestBody.getMedia() != null) {
            String str = "";
            for (String media : requestBody.getMedia()) {
                str += media;
                str += ",";
            }
            story.setMedia(str.substring(0, str.length() - 1));
        }
        storyDao.create(story);

        List<String> tags = requestBody.getTags();
        String tag_index = "";
        for (String tag : tags) {
            TagStory tagStory = new TagStory();
            tagStory.setTag_title(tag);
            tagStory.setStory_id(story.getStory_id());
            tag_index += tag + ", ";
            tagStoryDao.update(tagStory);
        }

        SearchEngine.addDoc(story.getStory_id(), story.getTitle(), story.getContent(), tag_index);

        return new StoryResponse(story, current_user, tagStoryDao, userDao, voteStoryDao);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/story/edit")
    public StoryResponse story_edit(@RequestBody StoryEditRequestBody requestBody, @RequestHeader HttpHeaders headers) {
        User current_user = getCurrentUser(headers, true);
        Story story = storyDao.getById(requestBody.getStory_id());
        if (current_user.getUser_id() != story.getOwner_id()) {
            throw new NotOwnerException();
        }

        //TODO Exception handling

        Date date = new Date();

        story.setStory_id(requestBody.getStory_id());
        story.setTitle(requestBody.getTitle());
        story.setContent(requestBody.getContent());
        story.setOwner_id(current_user.getUser_id());
        story.setCreate_date(date);
        story.setLast_edit_date(date);
        story.setLast_editor_id(current_user.getUser_id());
        if (requestBody.getMedia() != null) {
            String str = "";
            for (String media : requestBody.getMedia()) {
                str += media;
                str += ",";
            }
            story.setMedia(str.substring(0, str.length() - 1));
        }
        storyDao.edit(story);
        String tag_index = "";
        if (requestBody.getTags() != null) {
            List<String> tags = requestBody.getTags();

            for (String tag : tags) {
                tag_index += tag + ", ";
                TagStory tagStory = new TagStory();
                tagStory.setTag_title(tag);
                tagStory.setStory_id(story.getStory_id());
                tagStoryDao.update(tagStory);
            }
        }

        SearchEngine.removeDoc(story.getStory_id());
        SearchEngine.addDoc(story.getStory_id(), story.getTitle(), story.getContent(), tag_index);

        return new StoryResponse(story, current_user, tagStoryDao, userDao, voteStoryDao);
    }

    @RequestMapping("/story/get")
    public StoryResponse storyGet(@RequestBody StoryGetRequestBody requestBody, @RequestHeader HttpHeaders headers) {
        User current_user = getCurrentUser(headers, false);

        Story story = storyDao.getById(requestBody.getId());

        return new StoryResponse(story, current_user, tagStoryDao, userDao, voteStoryDao);
    }

    @RequestMapping("/search")
    public SearchResponse search(@RequestBody SearchRequestBody requestBody, @RequestHeader HttpHeaders headers) {
        User current_user = getCurrentUser(headers, false);
        //TODO Exception handling
        int page = requestBody.getPage();
        int size = requestBody.getSize();
        if (size < 1)
            size = 10;
        if (page < 1)
            page = 1;


        List<Long> story_ids = SearchEngine.search(requestBody.getQuery(), page, size);

        List<StoryResponse> responses = new LinkedList<>();
        for (long id : story_ids) {
            Story story = storyDao.getById(id);
            System.out.println(id);
            responses.add(new StoryResponse(story, current_user, tagStoryDao, userDao, voteStoryDao));
        }
        SearchResponse searchResponse = new SearchResponse();
        searchResponse.setResult(responses);
        return searchResponse;
    }


    @RequestMapping("/comment/get")
    public CommentResponse commentGet(@RequestBody CommentGetRequestBody requestBody) {
        Comment comment = commentDao.getById(requestBody.getCommentId());
        return new CommentResponse(comment, userDao);
    }

    @RequestMapping("/comment/new")
    public CommentResponse commentGet(@RequestBody CommentNewRequestBody requestBody, @RequestHeader HttpHeaders headers) {
        User current_user;
        try {
            String access_token;
            access_token = headers.get("access-token").get(0);
            current_user = userDao.getByAccessToken(access_token);
        } catch (NullPointerException | org.springframework.dao.EmptyResultDataAccessException e) {
            throw new InvalidAccessTokenException();
        }

        Comment comment = new Comment();
        Date date = new Date();
        comment.setContent(requestBody.getContent());
        comment.setCreate_date(date);
        comment.setOwner_id(current_user.getUser_id());
        comment.setStory_id(requestBody.getStoryId());
        comment.setLast_edit_date(date);
        commentDao.create(comment);
        return new CommentResponse(comment, userDao);
    }

    @RequestMapping("/story/report")
    public StoryReportResponse storyReport(@RequestBody StoryReportRequestBody requestBody, @RequestHeader HttpHeaders headers) {
        User current_user = getCurrentUser(headers, true);
        Story story = storyDao.getById(requestBody.getStory_id());

        reportStoryDao.reportStory(current_user, story);

        return new StoryReportResponse(story.getReport_count());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/story/vote")
    public VoteResponse storyVote(@RequestBody StoryVoteRequestBody requestBody, @RequestHeader HttpHeaders headers) {
        User current_user = getCurrentUser(headers, true);
        Story story = voteStoryDao.vote(current_user.getUser_id(), requestBody.getStory_id(), requestBody.getVote());
        return new VoteResponse(requestBody.getVote(), story);
    }


    @RequestMapping("/comment/list")
    public CommentListResponse commentList(@RequestBody CommentListRequestBody requestBody) {
        int page = requestBody.getPage();
        int size = requestBody.getSize();
        if (size < 1)
            size = 10;
        if (page < 1)
            page = 1;

        List<Comment> comments = commentDao.retrieveByStory(requestBody.getStory_id(), page, size);
        List<CommentResponse> responses = new LinkedList<>();

        for (Comment comment : comments) {
            responses.add(new CommentResponse(comment, userDao));
        }


        CommentListResponse commentListResponse = new CommentListResponse();
        commentListResponse.setResult(responses);
        return commentListResponse;
    }


    @RequestMapping("/admin/search/reindex")
    public void admin_search_reindex() {
        SearchEngine.removeAll();
        for (int i = 1; ; i++) {
            List<Story> stories = storyDao.getAllPaged(i, 20);
            for (Story story : stories) {
                List<String> tags = tagStoryDao.getTagTitlesByStoryId(story.getStory_id());
                String tag_index = "";
                for (String tag : tags)
                    tag_index += tag + ", ";
                SearchEngine.addDoc(story.getStory_id(), story.getTitle(), story.getContent(), tag_index);
            }
            if (stories.size() == 0)
                break;
        }

    }

    /**
     * Returns current user by using access-token
     *
     * @param headers The headers which contains access-token
     * @param notnull Whether the current_user can be null or not, if true it will not return null instead throw an exception
     * @return Current user
     */
    private User getCurrentUser(HttpHeaders headers, boolean notnull) {
        User current_user = null;
        try {
            String access_token;
            access_token = headers.get("access-token").get(0);
            current_user = userDao.getByAccessToken(access_token);
        } catch (NullPointerException | org.springframework.dao.EmptyResultDataAccessException ignored) {
            if (notnull)
                throw new InvalidAccessTokenException();
        }
        return current_user;
    }
}
