// Code: Helpbot Component
import HelpbotService from "@/services/HelpbotService";
import { StatusMessage } from "@/types";
import { useState } from "react";


const Helpbot: React.FC = () => {
    const [isOpen, setIsOpen] = useState(false);
    const [command, setCommand] = useState('');
    const [statusMessages, setStatusMessages] = useState<StatusMessage[]>([]);
    const [isCleared, setIsCleared] = useState(false);
    const [hasInformation, sethasInformation] = useState(false);
    const [information, setInformation] = useState<string>('');


    const validateForm = () => {
        let formIsValid = true;

        if (command == "") {
            formIsValid = false;
            setStatusMessages([{ message: "Command is required", type: "error" }]);
        }

        return formIsValid;
    };


    const getInformation = async () => {
        setStatusMessages([]);
        if(!validateForm()) {
            return;
        }
        const response = await HelpbotService.getCommandInfo(command);
        if (response.ok) {
            

            const data = await response.text();
            
            setInformation(data);
            // setStatusMessages([{ message: "Command sent", type: "success" }]);
            setIsCleared(false);
            sethasInformation(true);
        }
        else {
            setStatusMessages([{ message: "Command not sent", type: "error" }]);
            sethasInformation(false);
            const data = await response.text();
        }
    };

    const clearDisplay = () => {
        setStatusMessages([]);
        setCommand('');
        setIsCleared(true);
        sethasInformation(false);
    };
    

    return (
        <>
            <div>
                <button onClick={() => setIsOpen(!isOpen)} className="fixed bottom-4 right-4 font-bold bg-black border border-gray-300 rounded-md p-4 shadow-md text-white hover:bg-blue-500 hover:text-black focus:outline-none">
                    Help
                </button>
            </div>
            <div className={`fixed bottom-4 right-4 bg-black text-white border border-gray-300 rounded-xl p-4 shadow-md ${isOpen ? 'block' : 'hidden'}`}>
                <button onClick={() => setIsOpen(!isOpen)} className="absolute top-2 right-2 text-white hover:text-red-600 focus:outline-none px-2 font-bold">
                    X
                </button>
                <button className="font-bold hover:text-green-500" onClick={clearDisplay}>
                    clear
                </button>
                <h2 className="text-lg font-semibold mb-4 text-center">Car4Rental</h2>
                
                {statusMessages && (
                        <div className="text-center mb-2 font-bold">
                            <ul>
                                {statusMessages.map(({ message, type }, index) => (
                                    <li className={`status ${type === 'success' ? 'text-green-500' : 'text-red-500'}`} key={index}>
                                        {message}
                                    </li>
                                ))}
                            </ul>
                        </div>
                    )}
                <div className="bg-gray-100 rounded-md text-black max-w-md p-4">
                    {!hasInformation && (<div>
                    <h3 className="text-lg font-semibold mb-1">Commands</h3>
                    <ul className="list-none list-inside mb-4 max-h-[70px] overflow-y-auto">
                        <li>Type &quot;rent&quot; for more info</li>
                        <li>Type &quot;car&quot; for more info</li>
                        <li>Type &quot;rental&quot; for more info</li>
                        <li>Type &quot;role&quot; for more info</li>
                        <li>Type &quot;wallet&quot; for more info</li>
                    </ul>
                    </div>)}

                    {hasInformation && (<div>
                    <h3 className="text-lg font-semibold mb-1">Information</h3>
                    <p className="mb-2">{information}</p>
                    </div>)}

                    <div className="flex items-center">
                        <label htmlFor="helpInput" className="mr-2">Help: </label>
                        <input type="text" name="help"  id="helpInput" value={command}  onChange={event => setCommand(event.target.value)} className="w-full p-2 border border-gray-300 rounded-md"/>
                        <button onClick={getInformation} className="bg-black text-white px-4 py-2 rounded-md ml-2">Send</button>
                    </div>
                </div>
                
            </div>
        </>
    );
};


export default Helpbot;