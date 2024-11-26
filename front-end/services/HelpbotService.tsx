

// @GetMapping("/command/{command}")
//     public String getCommandInfo(@PathVariable String command) throws HelpbotServiceException{
//         try{
//             return helpbotService.getCommandInfo(command);
//         } catch (HelpbotServiceException e) {
//             return e.getMessage();
//         }
//     }

const getCommandInfo = async (command: string) => {
   
        const res = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/helpbot/command/${command}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/string'
            }
        });
        return res

       
}

const HelpbotService = {
    getCommandInfo
}

export default HelpbotService;