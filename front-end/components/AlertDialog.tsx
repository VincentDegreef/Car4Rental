import { Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from '@mui/material';
import React, { useEffect } from 'react';
import { GoAlertFill } from "react-icons/go";
import Image from 'next/image';

import alertLogo from '@/public/car4rent.webp';


type props = {
    readonly content: string
    readonly onClose: () => void
}

const AlertDialog:React.FC<props> = ({content, onClose}) =>{
    const [open, setOpen] = React.useState(true);

    const handleClose=()=>{
        onClose();
        setOpen(false);
        location.reload();
    }

    useEffect(() => {
        if(content === ''){
            setOpen(false);
        }
        if (content !== '') {
            setOpen(true);
        }
    }, [content]);

    return (
        <>
            <Dialog
                open={open} 
                onClose={handleClose}
                aria-labelledby="alert-dialog-title" 
                aria-describedby="alert-dialog-description"
                PaperProps={{ 
                    className: 'rounded-xl border-4 border-black text-white' 
                }}>
                    
                <DialogTitle id="alert-dialog-title" className="text-white text-center text-lg bg-black flex flex-col justify-center items-center">
                    <Image src={alertLogo} alt="Alert Logo" width={150} height={150} />
                </DialogTitle>
                <DialogContent className='bg-black text-white text-lg'>
                    {content}
                </DialogContent>
                <DialogActions className="bg-black flex justify-center items-center">
                    <button onClick={handleClose} className="p-2 m-2 bg-gray-200 text-black rounded hover:bg-gray-300 transition-colors">
                        Close
                    </button>
                </DialogActions>
            </Dialog>
        </>
    );
};

export default AlertDialog;