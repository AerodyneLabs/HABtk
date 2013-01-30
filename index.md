---
layout: page
title: Ravings of a Mad Engineer
tagline: High altitude ballooning, robotics, UAS, communications
---

I have just recently started moving stuff to GitHub, expect more content here soon.

## Recent Posts

<ul class="posts">
  {% for post in site.posts %}
    <li><span>{{ post.date | date_to_string }}</span> &raquo; <a href="{{ BASE_PATH }}{{ post.url }}">{{ post.title }}</a></li>
  {% endfor %}
</ul>