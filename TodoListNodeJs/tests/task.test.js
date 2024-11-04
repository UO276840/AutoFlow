const app = require('../app');
const {MongoClient} = require("mongodb");
const {mongoose} = require("mongoose");
const url = 'mongodb+srv://uo276840:2V8WlrqMWQK6Ry6d@todolist.1vrvjwl.mongodb.net/?retryWrites=true&w=majority&appName=ToDoList'
const request = require('supertest');

let conn= null;


beforeAll(async () => {
    conn = MongoClient.connect(url,{});
},30000);

afterEach(async () => {
    if (mongoose.connection.readyState === 1) {
        await mongoose.connection.db.dropDatabase();
    }
});

afterAll( async () => {
    await mongoose.connection.close();
    await mongoose.disconnect();
    await conn.then(dbClient=>{
        dbClient.close();
    })
});

describe('GET /todo', () => {
    test('should get all tasks', async () => {
        const taskData = { todoTask: 'Task 1' };
        await request(app)
            .post('/todo')
            .send(taskData);

        const res = await request(app).get('/todo');
        expect(res.status).toBe(200);
        expect(res.text).toContain('Task 1');
    });
});

describe('DELETE /todo/:id', () => {
    test('should delete a task', async () => {
        // Creamos una tarea de prueba en la base de datos
        const nonExistentTaskId = new mongoose.Types.ObjectId();
          await conn.then(dbClient=>{
            const database = dbClient.db("TFGNodeJs");
            const collectionName = 'ToDoList';
            const todosCollection = database.collection(collectionName);
            todosCollection.insertOne({
                _id: nonExistentTaskId,
                todoTask: 'Task 2',
            })

        });

        // Realizamos la solicitud de eliminación
        const res = await request(app).get(`/todo/delete/${nonExistentTaskId.toString()}`);

        // Verificamos el estado de la respuesta
        expect(res.status).toBe(200);
        expect(res.text).toContain('Task eliminado correctamente')
    }, 15000);

    test('should return 404 if task does not exist', async () => {
        const nonExistentTaskId = new mongoose.Types.ObjectId(); // Generamos un ID que no existe en la base de datos

        // Realizamos la solicitud de eliminación con un ID inexistente
        const res = await request(app).delete(`/todo/delete/${nonExistentTaskId}`);

        // Verificamos el estado de la respuesta
        expect(res.status).toBe(404);
    },15000);
});
