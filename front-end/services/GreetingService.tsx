const getGreetings = async () => {
    try{
        const res = await fetch(`${process.env.NEXT_PUBLIC_BACKEND_URL}/hello`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'

            }

            });
    
        if(!res.ok){
            throw new Error("Failed to fetch greetings")
        }
        return res.json()
    }catch(error){
        console.error("Error loading greeatings:",error)

    }
}

const GreetingService = {
    getGreetings
}

export default GreetingService;