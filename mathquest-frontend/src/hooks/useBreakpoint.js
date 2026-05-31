import { useState, useEffect } from 'react';

export function useBreakpoint() {
    const [bp, setBp] = useState('expanded');
    useEffect(() => {
        const check = () => {
            const w = window.innerWidth;
            if (w < 600) setBp('compact');
            else if (w < 840) setBp('Medium');
            else setBp('expanded')
        };
        check();
        window.addEventListener('resize', check);
        return () => window.removeEventListener('resize', check);
    }, []);
    return bp;
}