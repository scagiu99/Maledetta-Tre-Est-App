function onCreateDetailOfficialPostScreen(officialPost) {
  showScreen("#DetailOfficialPostScreen");
  $("#officialTitle").text(officialPost.authorName);
  $("#description").text(officialPost.comment);
  $("#timestamp").text(officialPost.datetime);
}
