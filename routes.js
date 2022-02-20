const express = require('express')
const routes = express.Router()

routes.get('/', (req, res) =>
{
    req.getConnection((err, conn) =>
    {
        if (err) return res.send(err)

        let v = new Date();
        const offset = v.getTimezoneOffset()
        v = new Date(v.getTime() - (offset * 60 * 1000))

        const f = v.toISOString().split('T')[0]
        conn.query('SELECT palabraReal, palabraSimple FROM usuarios where fecha_correspondiente = ?', [f], (err, rows) =>
        {
            if (err) return res.send(err)

            const palabaGuess = req.body.palabra
            const palabraReal = rows[0].palabraReal
            const palabraSimple = rows[0].palabraSimple

            let respuesta = []

            for (let index = 0; index < palabaGuess.length; index++)
            {
                if (palabaGuess[index] === palabraSimple[index])
                    respuesta.push(palabraReal[index])
                else
                    respuesta.push('')
            }
            res.send(respuesta)
        })
    })
})


module.exports = routes