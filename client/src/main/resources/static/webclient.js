const streamErr = e => {
    console.warn("error");
    console.warn(e);
}
//
// fetch("http://localhost:8082/data/5").then((response) => {
//     return can.ndjsonStream(response.body);
// }).then(dataStream => {
//     const reader = dataStream.getReader();
//     const read = result => {
//         if (result.done) {
//             return;
//         }
//         render(result.value);
//         reader.read().then(read, streamErr);
//     }
//     reader.read().then(read, streamErr);
// });
//

const render = value => {
    const div = document.createElement('div');
    div.append('stringValue:', JSON.stringify(value));
    document.querySelector('.report-sticker').append(div);
};

let lox
let functionFirstValue
let functionSecondValue
let iterationsValue
let orderValue

document.querySelector(".get-report-button").addEventListener("click", () => {

    functionFirstValue = document.querySelector(".first-function-input").value;
    functionSecondValue = document.querySelector(".second-function-input").value;
    iterationsValue = document.querySelector(".iterations-input").value;
    orderValue = document.querySelector(".order-input").value;

    fetch("http://localhost:9090/calculate", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            firstFunction: functionFirstValue,
            secondFunction: functionSecondValue,
            itr: iterationsValue,
            order: orderValue
        })
    }).then((response) => {
        return can.ndjsonStream(response.body);
    }).then(dataStream => {
        const reader = dataStream.getReader();
        const read = result => {
            if (result.done) {
                return;
            }
            render(result.value);
            reader.read().then(read, streamErr);
        }
        reader.read().then(read, streamErr);
    });

})

