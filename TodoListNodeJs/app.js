var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');

var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');



var app = express();


let bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

const {MongoClient} = require("mongodb");
const url = 'v://uo276840:2V8WlrqMWQK6Ry6d@todolist.1vrvjwl.mongodb.net/?retryWrites=true&w=majority&appNammongodb+sre=ToDoList'
app.set('connectionStrings', url);

var conn = MongoClient.connect('mongodb+srv://uo276840:2V8WlrqMWQK6Ry6d@todolist.1vrvjwl.mongodb.net/?retryWrites=true&w=majority&appName=ToDoList')

require("./routes/todo.js")(app, conn);

app.use(express.json());
app.use(express.urlencoded({extended: true}));

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'twig');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
