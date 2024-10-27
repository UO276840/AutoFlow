const {ObjectId} = require("mongodb");
module.exports = function(app, conn) {

    app.get("/todo", function (req, res) {
        const getData= conn.then(dbClient=>{
                    const database = dbClient.db("TFGNodeJs");
                    const collectionName = 'ToDoList';
                    const todosCollection = database.collection(collectionName);
                    return todosCollection.find().toArray();

                });


        getData.then(data=>{
            res.render("todo.twig", {
                data: data
            });
            res.body
        }).catch(error=>{
            res.send("Error al obtener los datos "+error)
        })

    });

    app.post("/todo", function (req, res) {
        conn.then(dbClient=>{
                const database = dbClient.db("TFGNodeJs");
                const collectionName = 'ToDoList';
                const todosCollection = database.collection(collectionName);
                var todos = 0
                todosCollection.find().toArray().then(
                    resultado=>todos+=resultado.length);
                todosCollection.insertOne({
                    todoTask: req.body.todoTask,
                })
                    .then(()=>res.redirect("/todo"))
                    .catch(err => res.send("Error al insertar " + err));

        });



    });


    app.get("/todo/delete/:id", (req, res) => {
        var requestedtodoId = req.body.todoId;

        conn.then(dbClient=>{
            const database = dbClient.db("TFGNodeJs");
            const collectionName = 'ToDoList';
            const todosCollection = database.collection(collectionName);
            todosCollection.deleteOne({_id:new ObjectId(req.params.id)}).then(()=>res.send("Task eliminado correctamente"));

        });
    });

};